package com.github.rehei.scala.dox.test

import scala.reflect._

import org.junit.Test

class TestClassTypeMatch {
  case class TestClass[T <: AnyRef]()(implicit clazzTag: ClassTag[T]) {
    def data(item: AnyRef) = {
      item match {
        case x if (classTag[T].runtimeClass.isInstance(x)) => true
        case _ => false
      }
    }
  }
  case class TestClassObject(name: String)
  @Test
  def test() = {
    val testObject = TestClassObject("peter")
    val testClass = TestClass[TestClassObject]()

    assume(testClass.data(testObject) == true)
    assume(testClass.data("bla") == false)

  }
}