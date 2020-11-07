package com.github.amraneze.jobs

import com.github.amraneze.entities.{ Lawn, Mower, Position }
import com.github.amraneze.entities.Movement.{ Coordination, LEFT, RIGHT }
import com.github.amraneze.data.Helper
import org.scalatest.Inside.inside

class MowerWorkerSpec extends Helper {

  it should "rotate the mower to the left for a given coordination" in {
    val coordination: Coordination = (generatePosition, 'N')
    val newCoordination: Coordination = MowerWorker.rotate(coordination, LEFT)
    inside(newCoordination) {
      case (position, direction) =>
        position should be(coordination._1)
        direction should be('W')
    }
  }

  it should "rotate the mower to the right for a given coordination" in {
    val coordination: Coordination = (generatePosition, 'N')
    val newCoordination: Coordination = MowerWorker.rotate(coordination, RIGHT)
    inside(newCoordination) {
      case (position, direction) =>
        position should be(coordination._1)
        direction should be('E')
    }
  }

  it should "throw an exception for an invalid orientation" in {
    val coordination: Coordination = (generatePosition, 'X')
    val caught = intercept[NoSuchElementException] {
      MowerWorker.rotate(coordination, LEFT)
    }
    assert(caught.getMessage === "X is an invalid direction.")
  }

  it should "move forward for a given coordination" in {
    val lawn: Lawn = generateLawn
    val coordination: Coordination = (generatePosition, 'N')
    val newCoordination: Coordination = MowerWorker.moveForward(lawn, coordination)
    inside(newCoordination) {
      case (position, direction) =>
        position should be(Position(coordination._1.x, coordination._1.y + 1))
        direction should be(coordination._2)
    }
  }

  it should "stop the mower if the new position is outside the lawn" in {
    val lawn: Lawn = generateLawn
    val coordination: Coordination = (Position(lawnMaxSurface, lawnMaxSurface), 'N')
    val newCoordination: Coordination = MowerWorker.moveForward(lawn, coordination)
    newCoordination should be(coordination)
  }

  it should "throw an exception for an invalid coordination" in {
    val lawn: Lawn = generateLawn
    val coordination: Coordination = (generatePosition, 'X')
    val caught = intercept[NoSuchElementException] {
      MowerWorker.moveForward(lawn, coordination)
    }
    assert(
      caught.getMessage === s"The given orientation ${coordination._2} with the position ${coordination._1} is an invalid."
    )
  }

  it should "start mowing the lawn" in {
    val lawn: Lawn = generateLawn
    val mowersWithExpectedResult: (Vector[Mower], Coordination) = generateMowerWithResult
    val actual = mowersWithExpectedResult._1.map { mower =>
      MowerWorker.start(lawn, mower, mower.commands)
    }
    actual.map { result =>
      inside(result) {
        case (_, coordination: Coordination) =>
          coordination should be(mowersWithExpectedResult._2)
      }
    }
  }

}
