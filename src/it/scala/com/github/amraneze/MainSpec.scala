package com.github.amraneze

import java.io.IOException

import com.github.amraneze.data.Helper
import com.github.amraneze.entities.Movement.Coordination
import org.scalatest.Inside.inside

class MainSpec extends Helper {

  it should "start mower and print the final positions" in {
    val expectedResult = validData(Symbol("anotherMowerWithResult"))
    val result: Vector[(String, Coordination)] = Main.startMowing(anotherMowerArgs)
    result.map { mowerWithCoordination =>
      inside(mowerWithCoordination) {
        case (_, coordination: Coordination) =>
          coordination should be(expectedResult._2)
      }
    }
  }

  it should "start mower and print the final positions from commands input" in {
    val result: Vector[(String, Coordination)] = Main.startMowing(args)
    result.map { mowerWithCoordination =>
      inside(mowerWithCoordination) {
        case (_, coordination: Coordination) =>
          coordination should (be(finalPositions(0)) or be(finalPositions(1)))
      }
    }
  }

  it should "start mower and print the final positions from file path" in {
    val result: Vector[(String, Coordination)] = Main.startMowing(argsFile)
    result.map { mowerWithCoordination =>
      inside(mowerWithCoordination) {
        case (_, coordination: Coordination) =>
          coordination should (be(finalPositions(0)) or be(finalPositions(1)))
      }
    }
  }

  it should "throw an exception if the command is unknown" in {
    val errorCommand = errors(Symbol("errorCommand"))
    val caught = intercept[RuntimeException] {
      Main.startMowing(errorCommand)
    }
    assert(
      caught.getMessage === s"The argument ${errorCommand.head} is not supported yet."
    )
  }

  it should "throw an exception if the file path is not found" in {
    val argsErrorFile = errors(Symbol("argsErrorFile"))
    val caught = intercept[IOException] {
      Main.startMowing(argsErrorFile)
    }
    assert(
      caught.getMessage === s"${argsErrorFile.tail.head} (The system cannot find the file specified)"
    )
  }
  it should "throw an exception if the lawn is malformed" in {
    val argsLawnSurfaceError = errors(Symbol("argsLawnSurfaceError"))
    val caught = intercept[IllegalArgumentException] {
      Main.startMowing(argsLawnSurfaceError)
    }
    assert(
      caught.getMessage === s"The lawn's surface ${argsLawnSurfaceError(1).split("""\\n""").toIndexedSeq.head} is not recognized."
    )
  }

  it should "throw an exception if the Mower position is malformed" in {
    val argsMowerPositionError = errors(Symbol("argsMowerPositionError"))
    val caught = intercept[IllegalArgumentException] {
      Main.startMowing(argsMowerPositionError)
    }
    assert(
      caught.getMessage === s"Mower's position ${argsMowerPositionError(1).split("""\\n""").toIndexedSeq.tail.head} is not recognized."
    )
  }

  it should "throw an exception if the Mower commands are different from F, L and R" in {
    val argsMowerCommandsError = errors(Symbol("argsMowerCommandsError"))
    val caught = intercept[MatchError] {
      Main.startMowing(argsMowerCommandsError)
    }
    caught.getMessage should include(
      argsMowerCommandsError(1).split("""\\n""").toIndexedSeq(2).toList.toString
    )
  }
}
