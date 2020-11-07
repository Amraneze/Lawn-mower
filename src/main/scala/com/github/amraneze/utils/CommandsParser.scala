package com.github.amraneze.utils

import java.io.IOException

import scala.language.{ postfixOps, reflectiveCalls }
import scala.util.matching.Regex
import scala.util.{ Failure, Success, Try }
import scala.annotation.tailrec
import scala.io.Source
import com.github.amraneze.entities.{ Lawn, Mower, Position }
import com.github.amraneze.entities

object CommandsParser {
  type ArgMap = Map[Symbol, Any]
  lazy val usage = """Usage: app --file filePath Or app --commands commandsString"""

  /**
	 * A function that will parse arguments in the App and returns a
	 * Map of symbols with their values. If an argument was given
	 * it will print the usage and throw an RuntimeException
	 * @param args the list of arguments that was given
	 * @return A map of field with their values
	 */
  @throws[RuntimeException]
  def parseArgs(args: Seq[String]): ArgMap = {
    if (args.isEmpty) {
      println(usage)
      throw new RuntimeException("No arguments were provided to the App.")
    }
    @tailrec
    def parseArgs(argsMap: ArgMap, argumentsList: Seq[String]): ArgMap =
      argumentsList match {
        case Seq() => argsMap
        case Seq("--file", file, tail @ _*) =>
          parseArgs(argsMap ++ Map(Symbol("file") -> file), tail)
        case Seq("--commands", string, tail @ _*) =>
          parseArgs(argsMap ++ Map(Symbol("commands") -> string), tail)
        case option +: _ =>
          println(usage)
          throw new RuntimeException(s"The argument $option is not supported yet.")
      }
    parseArgs(Map.empty, args)
  }

  /**
	 * Read a file line by line and returns a list of words in it.
	 * @param fileName, the file path to be read
	 * @throws IOException, if the file is not found or other exceptions
	 * @return a sequence of words
	 */
  @throws[IOException]
  def readFile(fileName: String): Seq[String] = {
    def using[A <: { def close(): Unit }, B](fileSource: A)(parseFile: A => B): B =
      try parseFile(fileSource)
      finally fileSource.close()
    using(Source.fromFile(fileName)) { source =>
      Try(source.getLines().toIndexedSeq) match {
        case Success(lines) => lines.filter(!_.trim.isEmpty).toList
        case Failure(error) =>
          throw new RuntimeException(s"Error while reading the file $error.")
      }
    }
  }

  /**
	 * A function that split a string to a list of strings, be careful to not use space
	 * as delimiter in case that the string contains words that has space in them.
	 * @param string the string to be split by break line
	 * @return a sequence of words of the string
	 */
  // Maybe to make the delimiter generic given by the user ???
  def parseString(string: String): Seq[String] =
    string.split("""\\n""").toIndexedSeq

  /**
	 * A function that takes arguments with symbol and parse them to
	 * have a list of commands for the lawn Mower
	 * @param args, the arguments given by the user
	 * @return the list of commands used to run the Lawn Mower
	 */
  def parseCommands(args: Seq[String]): Seq[String] = {
    val parsedArgs: ArgMap = CommandsParser.parseArgs(args)
    val file: Option[String] =
      parsedArgs.get(Symbol("file")).asInstanceOf[Option[String]].filter(!_.isEmpty)
    val string: Option[String] =
      parsedArgs.get(Symbol("commands")).asInstanceOf[Option[String]].filter(!_.isEmpty)

    (file, string) match {
      case (None, Some(str))      => parseString(str)
      case (Some(filePath), None) => readFile(filePath)
      // We won't have this case because it's handled in main
      // App as we go through all arguments but we don't know
      case _ => throw new RuntimeException("No data was given to the main App :(")
    }
  }

  /**
	 * Parse the surface of the lawn from the the first lines of commands
	 * @param surface a string that contains the starting position of the lawn
	 * @return a new Lawn instance with its starting position x & y
	 */
  def parseLawnSurface(surface: String): Lawn = {
    val sizeRegex: Regex = """([0-9]+) ([0-9]+)""".r
    surface match {
      case sizeRegex(width, height) => Lawn(width.toInt, height.toInt)
      case _ =>
        throw new IllegalArgumentException(
          s"The lawn's surface $surface is not recognized."
        )
    }
  }

  /**
	 * Parse the position of the Mower from the commands
	 * @param position a string which contains the current position of the Lawn Mower
	 * @param commands a string which contains the commands of the Lawn Mower
	 * @return a new Mower instance which contains the current position and orientation in the lawn
	 */
  def createMower(position: String, commands: String, index: Int): Mower = {
    val positionRegex: Regex = """([0-9]+) ([0-9]+) ([NEWS])""".r
    position match {
      case positionRegex(x, y, d) =>
        entities.Mower(id = index toString,
                       (Position(x.toInt, y.toInt), d.charAt(0)),
                       commands.toList)
      case _ =>
        throw new IllegalArgumentException(
          s"Mower's position $position is not recognized."
        )
    }
  }

  /**
	 * Prepare the commands parsed from the arguments of each mower
	 * @param commands a string that contains the commands to execute
	 * @return a list of each Mower instance
	 */
  def prepareCommands(commands: Seq[String]): (Lawn, Vector[Mower]) = {
    require(commands.length >= 3,
            throw new IllegalArgumentException(
              "The commands should contains at least 3 lines for a mower."
            ))
    lazy val lawn: Lawn = parseLawnSurface(commands.head)
    lazy val mowers: Vector[Mower] = parsePosition(commands.tail, Vector.empty[Mower])

    @tailrec
    def parsePosition(input: Seq[String], mowers: Vector[Mower]): Vector[Mower] =
      input match {
        case mower +: commands +: tail =>
          parsePosition(tail, mowers :+ createMower(mower, commands, mowers.length))
        case _ => mowers
      }
    (lawn, mowers)
  }

  /**
	 * Get the mowers and lawn from the given commands
	 * @param args a string or file that contains the commands to be given to the lawn mower
	* @return A tuple which contains the lawn and a list of mowers
	 */
  def getMowersAndLawn(args: Seq[String]): (Lawn, Vector[Mower]) = {
    val commands: Seq[String] = parseCommands(args)
    prepareCommands(commands)
  }
}
