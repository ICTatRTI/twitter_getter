package org.rti.rcd


import java.util.Date

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Collect tweets into json text files and write out as specified
 */
object Collect {

  private val gson = new Gson()
  private val intervalSecs =  3600 // write out a new set of tweets every interval
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
      val search_terms = sys.env("TWITTER_SEARCH_TERMS")
      terms_array = search_terms.split(",")
    } catch {
      case e: NoSuchElementException => {
        println("No search terms found, getting everything")
      }
    }



    // Size of output batches in seconds
    val outputBatchInterval = sys.env.get("OUTPUT_BATCH_INTERVAL").map(_.toInt).getOrElse(3600)

    // Date format for creating Hive partitions
    val outDateFormat = outputBatchInterval match {
      case 60 => new java.text.SimpleDateFormat("yyyy/MM/dd/HH/mm")
      case 3600 => new java.text.SimpleDateFormat("yyyy/MM/dd/HH")
    }

    // Number of output files per batch interval.
    val outputFiles = sys.env.get("OUTPUT_FILES").map(_.toInt).getOrElse(3)

    // Output directory
    val outputDir = sys.env.getOrElse("OUTPUT_DIR", "s3n://" + System.getProperty("rti.rcd.aws.bucketname"))


    // Echo settings to the user
    Seq(
      ("OUTPUT_DIR" -> outputDir),
      ("OUTPUT_FILES" -> outputFiles),
      ("OUTPUT_BATCH_INTERVAL" -> outputBatchInterval)).foreach {
      case (k, v) => println("%s: %s".format(k, v))
    }


    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth,terms_array)
      .map(gson.toJson(_))


    // Coalesce each batch into fixed number of files
    val coalesced = tweetStream.transform(rdd => rdd.coalesce(outputFiles))



    coalesced.foreachRDD((rdd, time) =>  {
      val count = rdd.count()
      if (count > 0) {
        print("count more than one")
        val outPartitionFolder = outDateFormat.format(new Date(time.milliseconds))
        rdd.saveAsTextFile("%s/%s".format(outputDir, outPartitionFolder))

      } else  {
        print("count not even one")
      }

    })



    ssc.start()
    ssc.awaitTermination()
  }
}
