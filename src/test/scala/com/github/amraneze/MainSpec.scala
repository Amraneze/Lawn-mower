package com.github.amraneze

import com.github.amraneze.data.Helper

class MainSpec extends Helper {
	it should "make a string from a mower coordination" in {
		val actual = Main.makeMowerCoordinationString(coordinationWithExpectedDisplayedResult._1)
	    actual should equal(coordinationWithExpectedDisplayedResult._2)
	}
}
