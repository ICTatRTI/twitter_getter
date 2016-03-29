package org.rti.rcd

import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Collect tweets into json text files and write out as specified
 */
object Collect {

  private val gson = new Gson()
  private val partitionsEachInterval = 5 // the number of output files written for each interval
  private val intervalSecs = 300 // write out a new set of tweets every interval
  private var terms_array = new Array[String](0)

  def main(args: Array[String]) {

    Utils.parseCommandLine(args)

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
    val sc = new SparkContext(conf)

    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", System.getProperty("rti.rcd.aws.accesskey"))
    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey",System.getProperty("rti.rcd.aws.secretaccesskey"))

    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    // Get search terms as comma delimited environment variable

    try {
      val search_terms = sys.env("MJ_SEARCH_TERMS")
      terms_array = search_terms.split(",")
    } catch {
      case e: NoSuchElementException => {
        println("No search terms found, getting everything")
      }
    }

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth,terms_array)
      .map(gson.toJson(_))
      
    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        outputRDD.saveAsTextFile("s3n://"+ System.getProperty("rti.rcd.aws.bucketname") + "/tweets-" + time.milliseconds.toString)

      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
