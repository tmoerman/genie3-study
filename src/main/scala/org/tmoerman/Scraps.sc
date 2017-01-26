import breeze.linalg._

val i_s = List(1, 2, 3)

val m =
  DenseMatrix(
    ( 1,  2,  3,  4,  5),
    ( 6,  7,  8,  9, 10),
    (11, 12, 13, 14, 15))

m(::, i_s)

m.toArray.grouped(3)