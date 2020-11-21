package com.github.rehei.scala.dox.tryout

class SomeTest extends MySealing {

  val X = fromDOI("bla")
  var Y = fromDOI("bli")

  object xyz {

    val BLI = fromDOI("bla")

  }

  class XYZ {
    val foo = fromDOI("bli")
  }

  object ZZZ extends XYZ

  val ZZZZZ = new XYZ

}

object SomeTest {

  def main(args: Array[String]): Unit = {

    val foo = new SomeTest()

    import foo._

    println(X.name())
    println(Y.name())
    println(xyz.BLI.name())
    println(ZZZ.foo.name())
    println(ZZZZZ.foo.name())

  }

}