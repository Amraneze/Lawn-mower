package com.github.amraneze.data

import java.io.File

import com.github.amraneze.entities
import com.github.amraneze.entities.Movement.Coordination
import com.github.amraneze.entities.{Lawn, Mower, Position}
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

  val file: File = FileHelper.writeToTempFile("5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF",
                                              Option("commands-"),
                                              Option(".txt"))
  val command: String = """5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF"""
  val randomFilePath: String = s"${randomStringFromCharList(10, orientations)}.${randomStringFromCharList(3, orientations)}".toLowerCase

  val args: Vector[String] =
    Vector("--commands", command)
  val argsFile: Vector[String] = Vector("--file", file.getPath)
  val anotherMowerArgs: Vector[String] =
    Vector("--commands", """10 10\n5 5 W\nLRFLFRRFRFLLRF""")

  val finalPositions: Vector[Coordination] =
    Vector((Position(1, 3), 'N'), (Position(5, 1), 'E'))

  val validData: Map[Symbol, (Vector[Mower], Coordination)] = Map(
    Symbol("mowerWithResult") -> ((Vector(
                                     Mower("0",
                                           (Position(1, 2), 'N'),
                                           Seq('L', 'F', 'L', 'F', 'L', 'F', 'L', 'F',
                                             'F'))
                                   ),
                                   (Position(1, 3), 'N'))),
    Symbol("anotherMowerWithResult") -> ((Vector(
                                            Mower("0",
                                                  (Position(5, 5), 'N'),
                                                  Seq('L', 'R', 'F', 'L', 'R', 'R', 'F',
                                                    'R', 'F', 'L', 'L', 'R', 'F'))
                                          ),
                                          (Position(5, 6), 'N')))
  )

  val errors: Map[Symbol, Vector[String]] = Map(
    Symbol("errorCommand") -> Vector("something"),
    Symbol("argsErrorFile") -> Vector("--file", """\something"""),
    Symbol("argsLawnSurfaceError") -> Vector("--commands", """N 5\n1 2 N\nLFLFLFLFF"""),
    Symbol("argsMowerPositionError") -> Vector("--commands", """5 5\nN 1 2\nLFLFLFLFF"""),
    Symbol("argsMowerCommandsError") -> Vector("--commands", """5 5\n1 2 N\nAFLFLFLFF""")
  )

  val coordinationWithExpectedDisplayedResult: (Coordination, String) = ((Position(10, 5), 'W'), "10 5 W")

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

  private def randomStringFromCharList(length: Int, chars: Vector[Char]): String =
    (1 to length).map(_ => chars(util.Random.nextInt(chars.length))).mkString("")

}
