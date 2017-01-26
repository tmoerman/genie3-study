package org.tmoerman.genie3

import java.lang.Math.sqrt

import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import smile.regression.RandomForest
import breeze.linalg.DenseMatrix

/**
  * @author Thomas Moerman
  */
object Genie3 {

  type ExpressionData = DenseMatrix[Double]

  type GRNMatrix = String // TODO
  type Index = Int
  type Score = Double

  /**
    * @param sc SparkContext.
    * @param expressionData Expression matrix.
    * @param geneNames A list of gene names or nil, interpreted as ALL.
    * @param regulatorNames A list of gene names which must be a subset of geneNames, or ALL.
    * @param K Specifies the number of selected attributes.
    * @param nrTrees The number of trees to grow in an ensemble, default 1000.
    * @param parallelWithSpark Use spark to parallelize the execution, default true.
    * @return Returns a matrix representing the GRN.
    */
  def apply(sc: SparkContext,
            expressionData: ExpressionData,
            geneNames: List[String] = Nil,
            regulatorNames: List[String] = Nil,
            K: String = "sqrt",
            nrTrees: Int = 1000,
            parallelWithSpark: Boolean = true): (GRNMatrix, Score) = {

    val geneCount = expressionData.size

    val broadcast = sc.broadcast(expressionData) // we need the entire data set on every node.

    val regulatorIndices = toRegulatorIndices(geneNames, regulatorNames, geneCount)

    sc
      .parallelize(0 to geneCount)
      .map(idx => computeRegulatorsRemote(broadcast, idx, regulatorIndices, nrTrees))

    ???
  }

  private[genie3] def toRegulatorIndices(geneNames: Iterable[String],
                                         regulators: Iterable[String],
                                         geneCount: Int) = {
    val pred = regulators.toSet

    geneNames
      .zipWithIndex
      .filter{ case (name, _) => pred(name) }
      .map(_._2)
      .toList match {
        case Nil  => 0 until geneCount
        case list => list
      }
  }

  private[genie3] val DEFAULT_MAX_NODES = 100
  private[genie3] val DEFAULT_NODE_SIZE = 5
  private[genie3] val DEFAULT_SUBSAMPLE = 1.0

  def computeRegulatorsRemote(broadcast: Broadcast[ExpressionData], targetGeneIndex: Index, regulatorIndices: Seq[Index], nrTrees: Int) =
    computeRegulatorsLocal(broadcast.value, targetGeneIndex, regulatorIndices, nrTrees)

  def computeRegulatorsLocal(expressionData: ExpressionData,
                             targetGeneIndex: Index,
                             regulatorIndices: Seq[Index],
                             nrTrees: Int) = {

    val trainingGeneIndices = regulatorIndices.filterNot(_ == targetGeneIndex)

    val trainingData = expressionData(::, trainingGeneIndices).toDenseMatrix.toArray.grouped(trainingGeneIndices.size).toArray
    val responseData = expressionData(::, targetGeneIndex).toArray

    val mtry: Int = sqrt(trainingGeneIndices.size).toInt

    //val rf = new RandomForest(trainingData, responseData, nrTrees, DEFAULT_MAX_NODES, DEFAULT_NODE_SIZE, mtry, DEFAULT_SUBSAMPLE)

    // val imp = rf.importance()

    Nil
  }

}