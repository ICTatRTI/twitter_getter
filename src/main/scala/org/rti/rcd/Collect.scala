package org.rti.rcd

import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Collect tweets into json text files and write out every so many 600/10 sec (adjust as needed) .
 */
object Collect {

  private var partNum = 0
  private val gson = new Gson()
  private val partitionsEachInterval = 10
  private val intervalSecs = 600

  def main(args: Array[String]) {

    Utils.parseCommandLine(args)

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
    val sc = new SparkContext(conf)

    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", System.getProperty("rti.rcd.aws.accesskey"))
    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey",System.getProperty("rti.rcd.aws.secretaccesskey"))

    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth)
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
