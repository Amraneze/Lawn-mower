package com.github.amraneze.data

import java.io.File

import com.github.amraneze.entities
import com.github.amraneze.entities.Movement.Coordination
import com.github.amraneze.entities.{ Lawn, Mower, Position }
import com.github.amraneze.helpers.FileHelper
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

trait Helper extends AnyFlatSpecLike with Matchers {

  val orientations: Vector[Char] = Vector('N', 'S', 'W', 'E')
  val commands: Vector[Char] = Vector('F', 'L', 'R')

  val lawnMaxSurface = 100
  val maxMowers = 100
  val maxCommands = 50
  val maxCoordination = 100

  val file: File = FileHelper.createTempFile("5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF",
                                             Option("commands-"),
                                             Option(".txt"))
  val command: String = """5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF"""

  val args: Vector[String] =
    Vector("--commands", command)
  val argsFile: Vector[String] = Vector("--file", file.getPath)

  val finalPositions: Vector[Coordination] =
    Vector((Position(1, 3), 'N'), (Position(5, 1), 'E'))

  val validData: Map[Symbol, (Vector[Mower], Coordination)] = Map(
    Symbol("mowerWithResult") -> ((Vector(
                                     Mower("0",
                                           (Position(1, 2), 'N'),
                                           Seq('L', 'F', 'L', 'F', 'L', 'F', 'L', 'F',
                                             'F'))
                                   ),
                                   (Position(1, 3), 'N')))
  )

  def generateLawn: Lawn = Lawn(lawnMaxSurface, lawnMaxSurface)

  def generatePosition: Position =
    Position(randomIntInRange(0, lawnMaxSurface), randomIntInRange(0, lawnMaxSurface))

  def generateCoordination: Coordination =
    (generatePosition, randomStringFromCharList(1, orientations).charAt(0))

  def generateCommands: Seq[Char] =
    randomStringFromCharList(randomIntInRange(1, maxCommands), commands)

  def generateRandomMowers: Vector[Mower] =
    (0 to randomIntInRange(1, maxMowers))
      .map(
        index => entities.Mower(index.toString, generateCoordination, generateCommands)
      )
      .toVector

  def generateOrientationsWithInvalidChar: Vector[Char] = orientations :+ '/'

  def generateRandomCoordination: Vector[Coordination] =
    (0 to randomIntInRange(1, maxCoordination)).toVector.map(_ => generateCoordination)

  def generateMowerWithResult: (Vector[Mower], Coordination) =
    validData(Symbol("mowerWithResult"))

  private def randomIntInRange(min: Int, max: Int): Int =
    new scala.util.Random().nextInt(max - min)

  private def randomStringFromCharList(length: Int, chars: Vector[Char]): String = {
    val sb = new StringBuilder
    for (_ <- 1 to length) {
      val randomNum = util.Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }

}
