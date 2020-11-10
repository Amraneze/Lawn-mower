package com.github.amraneze.utils

import java.io.{File, FileNotFoundException}

import CommandsParser.ArgMap
import com.github.amraneze.data.Helper
import com.github.amraneze.helpers.FileHelper

class CommandsParserSpec extends Helper {

  it should "parse a string returns a list of commands" in {
    val parsedStrings = CommandsParser.parseString(command)
    parsedStrings should contain theSameElementsAs command
      .replace("\\n", "\n")
      .split("\n")
      .toSeq
  }

  it should "read a file and returns a list of commands" in {
    val parsedStrings = CommandsParser.readFile(file.getPath)
    parsedStrings should contain theSameElementsAs command
      .replace("\\n", "\n")
      .split("\n")
      .toSeq
  }

  it should "throw a runtime exception if no argument was provided" in {
    the[RuntimeException] thrownBy {
      CommandsParser.parseArgs(Seq.empty)
    } should have message "No arguments were provided to the App."
  }

  it should "throw a runtime exception if an arguments not supported was provided" in {
    the[RuntimeException] thrownBy {
      CommandsParser.parseArgs(args ++ Seq("test"))
    } should have message "The argument test is not supported yet."

    the[RuntimeException] thrownBy {
      CommandsParser.parseArgs(argsFile ++ Seq("test"))
    } should have message "The argument test is not supported yet."
  }

  it should "parse arguments with commands as string" in {
    val parsedArgs: ArgMap = CommandsParser.parseArgs(args)
    parsedArgs(Symbol("commands")) should equal(args(1))
  }

  it should "parse arguments with commands as file" in {
    val parsedArgs: ArgMap = CommandsParser.parseArgs(argsFile)
    parsedArgs(Symbol("file")) should equal(argsFile(1))
  }

  it should "throw an illegal argument exception if the arguments are less than 3" in {
    the[IllegalArgumentException] thrownBy {
      CommandsParser.prepareCommands(Seq())
    } should have message "The commands should contains at least 3 lines for a mower."

    the[IllegalArgumentException] thrownBy {
      CommandsParser.prepareCommands(Seq("Something", "else"))
    } should have message "The commands should contains at least 3 lines for a mower."
  }

  it should "throw a file not found exception if the file is not found" in {
    val caught = intercept[FileNotFoundException] {
      CommandsParser.parseCommands(Seq("--file", randomFilePath))
    }
    caught.getMessage should include(s"$randomFilePath")
  }
}
