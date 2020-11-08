package com.github.amraneze

import sbt.{ Def, _ }

object Dependencies {

  val scalaTestVersion: String = "3.2.2"

  object Scala {
    val version: String = "2.13.3"
  }

  def testDependencies(deps: List[ModuleID]): Seq[ModuleID] = deps map (_ % "it, test")

  def scalaTest: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.scalatest" %% "scalatest" % scalaTestVersion :: Nil
  }

}
