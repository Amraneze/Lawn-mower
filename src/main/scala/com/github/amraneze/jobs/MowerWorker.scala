package com.github.amraneze.jobs

import com.github.amraneze.entities.Movement
import com.github.amraneze.entities.Movement._
import com.github.amraneze.entities.{ Lawn, Mower }

import scala.annotation.tailrec

object MowerWorker {

  /**
	* Rotate the mower given its coordination and the rotation type
	 * @param coordination the actual position of the mower
	 * @param rotation the type of rotation that should be used to
	 *                 rotate the mower, it needs to be LEFT or RIGHT
	 * @return the new coordination of the mower after rotating it
	 */
  def rotate(coordination: Coordination, rotation: Rotation): Coordination =
    (coordination._1, Movement.rotate(coordination._2, rotation))

  /**
	* Move the mower forward depending on its actual coordination
	 * we should also check if the new coordination is outside of
	 * the lawn, otherwise we will need to stop the mower and not
	 * do anything
	 *
	 * @param lawn the lawn that would be mowing
	 * @param coordination the actual coordination of the mower
	 * @return either the new coordination of the mower after
	 *         moving forward if it's allowed to move in the
	 *         lawn or the actual coordination of the mower
	 */
  def moveForward(lawn: Lawn, coordination: Coordination): Coordination = {
    val newCoordination: Coordination =
      (Movement.moveForward(coordination), coordination._2)
    if (lawn isAllowedToMove newCoordination._1) {
      newCoordination
    } else {
      coordination
    }
  }

  /**
	* Start mowing the lawn given the lawn, the mower and the commands
	 * that should be used to start the mower
	 * @param lawn the lawn where the mower is positioned
	 * @param mower the mower that should mow the lawn
	 * @param commands the list of commands that should be executed to
	 *                 mow the lawn
	 * @return the coordination of the mower after it stopped its mowing
	 */
  def start(lawn: Lawn, mower: Mower, commands: Seq[Char]): (String, Coordination) = {
    @tailrec
    def startRecursive(coordination: Coordination,
                       remainCommands: Seq[Char]): (String, Coordination) =
      remainCommands match {
        case 'L' :: tail => startRecursive(MowerWorker.rotate(coordination, LEFT), tail)
        case 'R' :: tail => startRecursive(MowerWorker.rotate(coordination, RIGHT), tail)
        case 'F' :: tail =>
          startRecursive(MowerWorker.moveForward(lawn, coordination), tail)
        case Nil => (mower.id, coordination)
      }

    startRecursive(mower.coordination, mower.commands)
  }
}
