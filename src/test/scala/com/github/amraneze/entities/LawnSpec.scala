package com.github.amraneze.entities

import com.github.amraneze.data.Helper
import org.scalatest.Inside.inside

class LawnSpec extends Helper {

  it should "create a lawn" in {
    val lawn: Lawn = Lawn(1, 1)
    inside(lawn) {
      case Lawn(width, height) =>
        width should be(1)
        height should be(1)
    }
  }

  it should "throw an exception for a lawn with 0 values" in {
    val caught = intercept[IllegalArgumentException] {
      Lawn(0, 0)
    }
    assert(caught.getMessage === "The lawn surface (0, 0) can not be negative or null.")
  }

  it should "throw an exception for a lawn with a null width" in {
    val caught = intercept[IllegalArgumentException] {
      Lawn(0, 10)
    }
    assert(caught.getMessage === "The lawn surface (0, 10) can not be negative or null.")
  }

  it should "throw an exception for a lawn with a null height" in {
    val caught = intercept[IllegalArgumentException] {
      Lawn(10, 0)
    }
    assert(caught.getMessage === "The lawn surface (10, 0) can not be negative or null.")
  }

  it should "throw an exception for a lawn with a negative width" in {
    val caught = intercept[IllegalArgumentException] {
      Lawn(-1, 0)
    }
    assert(caught.getMessage === "The lawn surface (-1, 0) can not be negative or null.")
  }

  it should "throw an exception for a lawn with negative height" in {
    val caught = intercept[IllegalArgumentException] {
      Lawn(0, -1)
    }
    assert(caught.getMessage === "The lawn surface (0, -1) can not be negative or null.")
  }

  it should "allow a mower to move within its surface" in {
    val lawn: Lawn = Lawn(10, 10)
    val position: Position = Position(10, 10)
    lawn isAllowedToMove position should be(true)
  }

  it should "not allow a mower to move with a negative position" in {
    val lawn: Lawn = Lawn(10, 10)
    val position: Position = Position(-1, -1)
    lawn isAllowedToMove position should be(false)
  }

  it should "not allow a mower to move away from its surface with a x higher than its width" in {
    val lawn: Lawn = Lawn(10, 10)
    val position: Position = Position(11, 2)
    lawn isAllowedToMove position should be(false)
  }

  it should "not allow a mower to move away from its surface with a y higher than its height" in {
    val lawn: Lawn = Lawn(10, 10)
    val position: Position = Position(0, 12)
    lawn isAllowedToMove position should be(false)
  }

}
