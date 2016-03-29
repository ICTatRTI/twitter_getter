package org.rti.rcd

import org.apache.commons.cli.{Options, ParseException, PosixParser}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.feature.HashingTF
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder
import org.apache.commons.cli.HelpFormatter


object Utils {

  val numFeatures = 1000
  val tf = new HashingTF(numFeatures)

  val CONSUMER_KEY = "consumerKey"
  val CONSUMER_SECRET = "consumerSecret"
  val ACCESS_TOKEN = "accessToken"
  val ACCESS_TOKEN_SECRET = "accessTokenSecret"
  var AWS_ACCESS_KEY = "awsAccessKey"
  var AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey"
  var AWS_BUCKET_NAME = "awsBucketName"

  val THE_OPTIONS = {
    val options = new Options()
    options.addOption(CONSUMER_KEY, true, "Twitter OAuth Consumer Key")
    options.addOption(CONSUMER_SECRET, true, "Twitter OAuth Consumer Secret")
    options.addOption(ACCESS_TOKEN, true, "Twitter OAuth Access Token")
    options.addOption(ACCESS_TOKEN_SECRET, true, "Twitter OAuth Access Token Secret")
    options.addOption(AWS_ACCESS_KEY, true, "AWS Access Key")
    options.addOption(AWS_SECRET_ACCESS_KEY, true, "AWS Secret Access Key")
    options.addOption(AWS_BUCKET_NAME, true, "AWS S3 Bucket Name")
    options
  }

  def parseCommandLine(args: Array[String]) = {
    val parser = new PosixParser
    try {
      val cl = parser.parse(THE_OPTIONS, args)
      if (cl.hasOption(CONSUMER_KEY) &&
          cl.hasOption(CONSUMER_SECRET) &&
          cl.hasOption(ACCESS_TOKEN) &&
          cl.hasOption(ACCESS_TOKEN_SECRET) &&
          cl.hasOption(AWS_ACCESS_KEY) &&
          cl.hasOption(AWS_SECRET_ACCESS_KEY) &&
          cl.hasOption(AWS_BUCKET_NAME)
      ){
        // fine
      }
      else {
        help
        System.exit(0)
      }

      System.setProperty("twitter4j.oauth.consumerKey", cl.getOptionValue(CONSUMER_KEY))
      System.setProperty("twitter4j.oauth.consumerSecret", cl.getOptionValue(CONSUMER_SECRET))
      System.setProperty("twitter4j.oauth.accessToken", cl.getOptionValue(ACCESS_TOKEN))
      System.setProperty("twitter4j.oauth.accessTokenSecret", cl.getOptionValue(ACCESS_TOKEN_SECRET))
      System.setProperty("rti.rcd.aws.accesskey", cl.getOptionValue(AWS_ACCESS_KEY))
      System.setProperty("rti.rcd.aws.secretaccesskey", cl.getOptionValue(AWS_SECRET_ACCESS_KEY))
      System.setProperty("rti.rcd.aws.bucketname", cl.getOptionValue(AWS_BUCKET_NAME))

    } catch {
      case e: ParseException =>
        help
        System.exit(0)
    }
  }

  def help  ={
    val formater = new HelpFormatter()
    formater.printHelp("Main", THE_OPTIONS)
    System.exit(0)
  }


  def getAuth = {
    Some(new OAuthAuthorization(new ConfigurationBuilder().build()))
  }

  /**
    * Create feature vectors by turning each tweet into bigrams of characters (an n-gram model)
    * and then hashing those to a length-1000 feature vector that we can pass to MLlib.
    * This is a common way to decrease the number of features in a model while still
    * getting excellent accuracy (otherwise every pair of Unicode characters would
    * potentially be a feature).
    */
  def featurize(s: String): Vector = {
    tf.transform(s.sliding(2).toSeq)
  }

  object IntParam {
    def unapply(str: String): Option[Int] = {
      try {
        Some(str.toInt)
      } catch {
        case e: NumberFormatException => None
      }
    }
  }

}