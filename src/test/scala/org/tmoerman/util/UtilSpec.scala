package org.tmoerman.util

import breeze.linalg._
import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Thomas Moerman
  */
class UtilSpec extends FlatSpec with Matchers {

  behavior of "parse"

  val wd = "src/test/resources/"
  val data = wd + "data.txt"

  behavior of "parseMatrix"

  it should "parse data.txt correctly" in {
    val (m, h) = Util.parseMatrix(data, header = true).get

    println(m(0 to 3, *))

    h.length shouldBe 10
  }

}