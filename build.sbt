name := "scala-dox"

// set version to environment variable $tag 
version := sys.props.getOrElse("tag", default = "0.0.0")

organization := "com.github.rehei"

scalaVersion := "2.12.12"
crossScalaVersions := Seq("2.12.12")

resolvers += "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "staging" at "https://oss.sonatype.org/content/repositories/staging"
resolvers += "releases" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += Resolver.bintrayRepo("rehei", "maven")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= {
  Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
    "org.apache.commons" % "commons-lang3" % "3.11",
    "commons-io" % "commons-io" % "2.8.0",
    "org.jbibtex" % "jbibtex" % "1.0.17",
    "org.scalaj" %% "scalaj-http" % "2.4.2",
    "com.github.marschall" % "memoryfilesystem" % "2.1.0" % Test, 
    "com.novocode" % "junit-interface" % "0.11" % Test,
    "junit" % "junit" % "4.13" % Test

  )
}

EclipseKeys.withSource := true

