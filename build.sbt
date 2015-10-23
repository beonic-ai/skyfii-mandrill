sonatypeSettings

name := "skyfii-mandrill"

organization := "io.skyfii"

version := "0.0.3"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.0.4" withSources() withJavadoc(),
  "joda-time" % "joda-time" % "2.8.2" withSources() withJavadoc(),
  "org.joda" % "joda-convert" % "1.2" withSources() withJavadoc(),
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2" withSources() withJavadoc(),
  "ch.qos.logback" % "logback-classic" % "1.1.2" withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "2.2.1" % "test" withSources() withJavadoc()
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "content/repositories/snapshots")
  } else {
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra :=
  <url>http://github.com/skyfii/skyfii-mandrill</url>
    <licenses>
      <license>
        <name>Apache License 2.0</name>
        <url>http://opensource.org/licenses/Apache-2.0</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/skyfii/skyfii-mandrill.git</connection>
      <developerConnection>scm:git:git@github.com:skyfii/skyfii-mandrill.git</developerConnection>
      <url>github.com/skyfii/skyfii-mandrill</url>
    </scm>
    <developers>
      <developer>
        <id>eggplantbr</id>
        <name>Jose Siqueira</name>
        <url>http://skyfii.io</url>
      </developer>
    </developers>


initialCommands := "import io.skyfii.mandrill._"

