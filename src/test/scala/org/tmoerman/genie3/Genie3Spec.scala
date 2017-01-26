package org.tmoerman.genie3

import java.lang.Math._

import org.scalatest.{Matchers, FlatSpec}

import Genie3._
import org.tmoerman.util.Util.parseMatrix
import smile.regression.{RandomForest => SmileRF}

/**
  * @author Thomas Moerman
  */
class Genie3Spec extends FlatSpec with Matchers {

  behavior of "regulatorIndices"

  val genes = List("A", "B", "C", "D", "E")
  val regulators = List("B", "D")
  val geneCount = 5
  val ALL = (0 until geneCount).toList

  it should "calculate correct indices when both lists are specified" in {
    toRegulatorIndices(genes, regulators, geneCount) shouldBe List(1, 3)
  }

  it should "calculate correct indices when no regulators are specified" in {
    toRegulatorIndices(genes, Nil, geneCount) shouldBe ALL
  }

  it should "calculate correct indices when no genes are specified" in {
    toRegulatorIndices(Nil, regulators, geneCount) shouldBe ALL
  }

  it should "calculate correct indices when no genes nor regulators are specified" in {
    toRegulatorIndices(Nil, Nil, geneCount) shouldBe ALL
  }



  behavior of "computeRegulatorsLocal"

  val wd = "src/test/resources/"
  val data = wd + "data.txt"

  it should "pass smoke test" in {

    val (expression, genes) = parseMatrix(data).get

    val regulatorIndices = toRegulatorIndices(genes, Nil, genes.length)

    val targetGeneIndex = 0

    val trainingGeneIndices = regulatorIndices.filterNot(_ == targetGeneIndex)

    val trainingData = expression(::, trainingGeneIndices).toDenseMatrix.toArray.grouped(trainingGeneIndices.size).toArray
    val responseData = expression(::, targetGeneIndex).toArray

    val mtry: Int = sqrt(trainingGeneIndices.size).toInt

    val smileRF = new SmileRF(null, trainingData, responseData, 100, 100, 5, mtry, 1.0)

    val wekaRF =

    println(smileRF.importance().toList)
  }

}