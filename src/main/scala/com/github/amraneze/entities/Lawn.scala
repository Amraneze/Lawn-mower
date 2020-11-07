package com.github.amraneze.entities

/**
 * A lawn entity which requires the width and height to create one
 * PS: The width and height should be greater than 0 otherwise it
 * would be illogical to have a lawn with zero width or zero height
 * (we will have a line)
 *
 * @param width the width of the lawn should be greater than 0
 * @param height the height of the lawn should be greater than 0
 */
case class Lawn(width: Int, height: Int) {
  require(width > 0 && height > 0,
          throw new IllegalArgumentException(
            s"The lawn surface ($width, $height) can not be negative or null."
          ))
  def isAllowedToMove(position: Position): Boolean =
    position.x <= height && position.x >= 0 && position.y <= width && position.y >= 0

}
