import sbt._
import Keys._
import com.github.amraneze.Dependencies._

lazy val root = project
	.in(file("."))
	.configs(IntegrationTest)
	.settings(
		name := "Lawn Mower",
		version := "0.0.1",
		Defaults.itSettings,
		mainClass in (Compile, run) := Some("com.github.amraneze.Main"),
		libraryDependencies ++= testDependencies(scalaTest.value),
		Seq(
			organization := "ContentSquare",
			scalaVersion := Scala.version
		),
		IntegrationTest / dependencyClasspath :=
			(IntegrationTest / dependencyClasspath).value ++ (Test / exportedProducts).value
	)


