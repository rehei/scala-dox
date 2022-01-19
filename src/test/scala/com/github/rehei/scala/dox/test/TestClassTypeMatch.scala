package com.github.rehei.scala.dox.test

import scala.reflect._

import org.junit.Test
import scala.reflect.api._

class TestClassTypeMatch {
  case class TestClass[T <: AnyRef]()(implicit clazzTag: ClassTag[T]) {
    def dataClass(item: AnyRef) = {
      item match {
        case x if (classTag[T].runtimeClass.isInstance(x)) => true
        case y: T => true
        case _ => false
      }
    }

  }
  case class TestClassObject(name: String)
  @Test
  def test() = {
    val testObject = TestClassObject("peter")
    val testClass = TestClass[TestClassObject]()

    assume(testClass.dataClass(testObject) == true)
    assume(testClass.dataClass("bla") == false)

  }
}