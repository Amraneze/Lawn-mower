package com.github.amraneze

import sbt._

case class Module(groupID: String, artifactID: String, version: String) {

	def java: Def.Initialize[ModuleID] = Def.setting(groupID % artifactID % version)
	def scala: Def.Initialize[ModuleID] = Def.setting(groupID %% artifactID % version)

}

object Dependencies {

	object Scala {
		val version: String = "2.13.3"
	}

	object ScalaTest {
		private val version: String = "3.2.2"
		private val groupID: String = "org.scalatest"
		val core: Module = Module(groupID, "scalatest", version)
	}

}