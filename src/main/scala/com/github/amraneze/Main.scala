package com.github.amraneze

import com.github.amraneze.entities.Movement.Coordination
import com.github.amraneze.jobs.MowerWorker
import com.github.amraneze.utils.CommandsParser

object Main extends App {

  def startMowing(arguments: Seq[String]): Vector[(String, Coordination)] =
    CommandsParser.getMowersAndLawn(arguments) match {
      case (lawn, mowers) =>
        mowers.map { mower =>
          MowerWorker.start(lawn, mower, mower.commands)
        }
      case _ =>
        throw new IllegalArgumentException("An issue with the parsing the arguments")
    }

  def makeMowerCoordinationString(coordination: Coordination): String =
    s"${coordination._1.x} ${coordination._1.y} ${coordination._2}"

  startMowing(args.toIndexedSeq) foreach {
    case (_, coordination) => println(makeMowerCoordinationString(coordination))
  }
}
