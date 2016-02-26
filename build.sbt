
name := "sbt_temp"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.0",
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.0",
  "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.0",
  "com.google.code.gson" % "gson" % "1.7.1")
