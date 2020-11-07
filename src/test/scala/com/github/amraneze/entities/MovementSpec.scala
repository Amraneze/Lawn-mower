package com.github.amraneze.entities

import Movement.{ LEFT, RIGHT }
import com.github.amraneze.data.Helper

class MovementSpec extends Helper {

  it should "rotate 90° of the actual Movement to the left" in {
    generateOrientationsWithInvalidChar.foreach {
      case 'N' => Movement.rotate('N', LEFT) shouldEqual 'W'
      case 'W' => Movement.rotate('W', LEFT) shouldEqual 'S'
      case 'S' => Movement.rotate('S', LEFT) shouldEqual 'E'
      case 'E' => Movement.rotate('E', LEFT) shouldEqual 'N'
      case _   => an[NoSuchElementException] should be thrownBy Movement.rotate('/', LEFT)
    }
  }

  it should "rotate 90° of the actual Movement to the right" in {
    generateOrientationsWithInvalidChar.foreach {
      case 'N' => Movement.rotate('N', RIGHT) shouldEqual 'E'
      case 'W' => Movement.rotate('W', RIGHT) shouldEqual 'N'
      case 'S' => Movement.rotate('S', RIGHT) shouldEqual 'W'
      case 'E' => Movement.rotate('E', RIGHT) shouldEqual 'S'
      case _   => an[NoSuchElementException] should be thrownBy Movement.rotate('/', RIGHT)
    }
  }

  it should "move forward" in {
    generateRandomCoordination.foreach {
      case (position, 'N') =>
        Movement.moveForward((position, 'N')) shouldEqual Position(position.x,
                                                                   position.y + 1)
      case (position, 'W') =>
        Movement.moveForward((position, 'W')) shouldEqual Position(position.x - 1,
                                                                   position.y)
      case (position, 'S') =>
        Movement.moveForward((position, 'S')) shouldEqual Position(position.x,
                                                                   position.y - 1)
      case (position, 'E') =>
        Movement.moveForward((position, 'E')) shouldEqual Position(position.x + 1,
                                                                   position.y)
      case coordination =>
        an[NoSuchElementException] should be thrownBy Movement.moveForward(coordination)
    }
  }
}
