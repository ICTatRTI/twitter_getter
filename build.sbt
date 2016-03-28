
name := "twitter_getter"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.4.0",
  "org.apache.spark" %% "spark-mllib" % "1.4.0",
  "org.apache.spark" %% "spark-sql" % "1.4.0",
  "org.apache.spark" %% "spark-streaming" % "1.4.0",
  "org.apache.spark" %% "spark-streaming-twitter" % "1.4.0",
  "com.google.code.gson" % "gson" % "2.3",
  "org.twitter4j" % "twitter4j-core" % "3.0.3",
  "commons-cli" % "commons-cli" % "1.2"
)

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"
