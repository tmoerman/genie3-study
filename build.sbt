import sbt.Keys._

organization := "org.tmoerman"

name := "genie3-spark"

description :=
  """A distributed version of Genie3 using Apache Spark.
    |
    |The GENIE3 method is described in the following paper:
    |Huynh-Thu V. A., Irrthum A., Wehenkel L., and Geurts P. (2010) Inferring reg-
    |ulatory networks from expression data using tree-based methods. PLoS ONE,
    |5(9):e12776.
  """.stripMargin

version := "1.0"

scalaVersion := "2.11.8"

sparkVersion := "2.0.2"

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

parallelExecution in Test := false

scalacOptions ++= Seq("-Xmax-classfile-name","78") // http://stackoverflow.com/questions/28565837/filename-too-long-sbt

sparkComponents ++= Seq("mllib")

libraryDependencies ++= Seq(

  "org.scalactic" %% "scalactic" % "3.0.1",

  "com.github.haifengl" %% "smile-scala" % "1.2.2",

  "nz.ac.waikato.cms.weka" % "weka-stable" % "3.8.0",

  "org.scalatest"   %% "scalatest" % "3.0.1" % "test",
  "com.holdenkarau" %% "spark-testing-base" % "2.0.0_0.6.0" % "test"

)

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

