
name := "twitter_getter"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.0",
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.0",
  "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.0",
  "org.twitter4j" % "twitter4j-core" % "3.0.3",
  "commons-cli" % "commons-cli" % "1.2",
  "com.google.code.gson" % "gson" % "1.7.1")


resolvers += "Akka Repository" at "http://repo.akka.io/releases/"