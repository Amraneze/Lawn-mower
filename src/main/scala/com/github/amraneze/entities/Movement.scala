package com.github.amraneze.entities

object Movement {

  sealed trait Rotation

  final case object LEFT extends Rotation
  final case object RIGHT extends Rotation

  type Coordination = (Position, Char)

  /**
	 * Move the mower depending on the previous orientation
	 * @param coordination actual coordination of the mower (position & orientation)
	 * @return the new position after the mower moved
	 */
  def moveForward(coordination: Coordination): Position =
    coordination match {
      case (position, 'N') => Position(position.x, position.y + 1)
      case (position, 'W') => Position(position.x - 1, position.y)
      case (position, 'S') => Position(position.x, position.y - 1)
      case (position, 'E') => Position(position.x + 1, position.y)
      case _ =>
        throw new NoSuchElementException(
          s"The given orientation ${coordination._2} with the position ${coordination._1} is an invalid."
        )
    }

  /**
	 * Rotate the mower with a given orientation and rotation
	 * @param orientation direction of the mower, it should be one of [N, S, E, W]
	 * @param rotation rotation of the mower it should be Left or Right
	 * @return the new orientation of the mower
	 */
  def rotate(orientation: Char, rotation: Rotation): Char =
    (orientation, rotation) match {
      case ('N', LEFT)  => 'W'
      case ('W', LEFT)  => 'S'
      case ('S', LEFT)  => 'E'
      case ('E', LEFT)  => 'N'
      case ('N', RIGHT) => 'E'
      case ('W', RIGHT) => 'N'
      case ('S', RIGHT) => 'W'
      case ('E', RIGHT) => 'S'
      case _            => throw new NoSuchElementException(s"$orientation is an invalid direction.")
    }
}

case class Position(x: Int, y: Int)
case class Movement(position: Position, direction: Char)
