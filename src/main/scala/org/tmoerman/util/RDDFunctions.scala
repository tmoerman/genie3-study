package org.tmoerman.util

import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

/**
  * @author Thomas Moerman
  */
object RDDFunctions {

  implicit def pimpRDD[T: ClassTag](rdd: RDD[T]): RDDFunctions[T] = new RDDFunctions[T](rdd)

}

class RDDFunctions[T: ClassTag](val rdd: RDD[T]) {

  def drop(n: Int) = rdd.mapPartitionsWithIndex{ (idx, it) => if (idx == 0) it.drop(n) else it }

}