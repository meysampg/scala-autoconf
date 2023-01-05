organization := "com.github.meysampg"
name := "autoconf"
version := "0.0.1"
scalaVersion := "2.12.10"

githubOwner := "meysampg"
githubRepository := "scala-autoconf"
githubTokenSource := TokenSource.GitConfig("github.token")

lazy val root = (project in file("."))
  .settings(
	  name := "autoconf"
  )

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.12.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % Test
