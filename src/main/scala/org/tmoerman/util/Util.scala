package org.tmoerman.util

import java.io.File

import breeze.linalg.DenseMatrix

import scala.io.Source.fromFile
import scala.util.{Failure, Success, Try}

/**
  * @author Thomas Moerman
  */
object Util {

  def parseMatrix(file: String, separator: Char = '\t', header: Boolean = true): Try[(DenseMatrix[Double], List[String])] = try {

    val cols = if (header) fromFile(file).getLines.next.split(separator).map(_.trim).toList else Nil

    val vals = breeze.linalg.csvread(new File(file), separator, skipLines = if (header) 1 else 0)

    Success((vals, cols))
  } catch {
    case e: Exception => Failure(e)
  }

}