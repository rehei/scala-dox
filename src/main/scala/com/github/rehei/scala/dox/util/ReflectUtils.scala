package com.github.rehei.scala.dox.util

import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

object ReflectUtils {

  def isSetter[T](m: MethodSymbol)(implicit typeTag: TypeTag[T]) = {
    m.toString().endsWith("_=") && m.paramLists(0)(0).typeSignature <:< typeTag.tpe
  }

  def isGetter[T](m: MethodSymbol)(implicit typeTag: TypeTag[T]) = {
    m.paramLists.headOption.map(_.isEmpty).getOrElse(true) && m.returnType <:< typeTag.tpe
  }

}