name := "scala-dox"

// set version to environment variable $tag 
version := sys.props.getOrElse("tag", default = "0.0.0")

organization := "com.github.rehei"

scalaVersion := "2.12.3"

resolvers += "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "staging" at "https://oss.sonatype.org/content/repositories/staging"
resolvers += "releases" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += Resolver.bintrayRepo("rehei", "maven")


libraryDependencies ++= {
  Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.1.1",
    "commons-io" % "commons-io" % "2.6",
    "org.reflections" % "reflections" % "0.9.12",
    "com.github.rehei" %% "scala-macros" % "0.9.3",
    "org.jbibtex" % "jbibtex" % "1.0.17",
    "org.scalaj" %% "scalaj-http" % "2.4.2"
  )
}

EclipseKeys.withSource := true

