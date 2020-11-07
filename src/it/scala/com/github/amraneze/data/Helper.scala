package com.github.amraneze.data

import java.io.File

import com.github.amraneze.entities.Movement.Coordination
import com.github.amraneze.entities.{ Mower, Position }
import com.github.amraneze.helpers.FileHelper
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

trait Helper extends AnyFlatSpecLike with Matchers {

  val file: File = FileHelper.createTempFile("5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF",
                                             Option("commands-"),
                                             Option(".txt"))
  val command: String = """5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF"""

  val args: Vector[String] =
    Vector("--commands", command)
  val argsFile: Vector[String] = Vector("--file", file.getPath)
  val anotherMowerArgs: Vector[String] =
    Vector("--commands", """10 10\n5 5 W\nLRFLFRRFRFLLRF""")

  val finalPositions: Vector[Coordination] =
    Vector((Position(1, 3), 'N'), (Position(5, 1), 'E'))

  val validData: Map[Symbol, (Vector[Mower], Coordination)] = Map(
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

}
