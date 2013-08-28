import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "javabin-server"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.newrelic.agent.java" % "newrelic-agent" % "2.21.3",
    "org.pegdown" % "pegdown" % "1.4.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
