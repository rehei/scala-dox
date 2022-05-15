package com.github.rehei.scala.dox.text

object TextObjectGreekLetter {
  
  protected var i = 0
  
  val ALPHA = generate()
  val BETA = generate()
  val GAMMA = generate()
  val DELTA = generate()
  val EPSILON = generate()
  val ZETA = generate()
  val ETA = generate()
  val THETA = generate()
  val IOTA = generate()
  val KAPPA = generate()
  val LAMBDA = generate()
  val MU = generate()
  val NU = generate()
  val XI = generate()
  val OMICRON = generate()
  val PI = generate()
  val RHO = generate()
  val SIGMA = generate()
  val TAU = generate()
  val UPSILON = generate()
  val PHI = generate()
  val CHI = generate()
  val PSI = generate()
  val OMEGA = generate()
  
  def generate() = {
    i = i + 1
    TextObjectGreekLetter(i)
  }
  
}


case class TextObjectGreekLetter(id: Int)