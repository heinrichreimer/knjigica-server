package de.unihalle.informatik.bigdata.knjigica

import org.junit.Assert


infix fun Any?.`should equal`(theOther: Any?) = Assert.assertEquals(theOther, this)
infix fun Any?.`should not equal`(theOther: Any?) = Assert.assertNotEquals(theOther, this)
