package com.github.amraneze

import com.github.amraneze.entities.Movement.Coordination
import com.github.amraneze.jobs.MowerWorker
import com.github.amraneze.utils.CommandsParser

object Main extends App {

  def startMowing(arguments: Seq[String]): Vector[(String, Coordination)] =
    (CommandsParser.getMowersAndLawn(arguments) match {
      case (lawn, mowers) =>
        mowers.map { mower =>
          MowerWorker.start(lawn, mower, mower.commands)
        }
      case _ =>
        throw new IllegalArgumentException("An issue with the parsing the arguments")
    })

  startMowing(args.toIndexedSeq) foreach {
    case (mowerId, coordination) =>
      println(
        s"""Mower $mowerId has finished it's job and its final position is $coordination"""
      )
  }
}
