package com.github.rehei.scala.dox.test.util

import scala.reflect.ClassTag
import org.junit.Assert

object Checking {
  def testException[T](callback: () => Unit)(implicit clazztag: ClassTag[T]) {
    try {
      callback()
      Assert.fail()
    } catch {
      case ex: Throwable => assert(ex.getClass() == clazztag.runtimeClass)
    }
  }
}