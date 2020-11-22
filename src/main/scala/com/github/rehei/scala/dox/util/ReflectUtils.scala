package com.github.rehei.scala.dox.util

import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

object ReflectUtils {

  protected val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

  def isSetter[T](m: MethodSymbol)(implicit typeTag: TypeTag[T]) = {
    m.toString().endsWith("_=") && m.paramLists(0)(0).typeSignature <:< typeTag.tpe
  }

  def isGetter[T](m: MethodSymbol)(implicit typeTag: TypeTag[T]) = {
    m.paramLists.headOption.map(_.isEmpty).getOrElse(true) && m.returnType <:< typeTag.tpe
  }

  def reflectInstance(model: Any) = {
    globalRuntimeM.reflect(model)
  }

  def applyModule(model: Any, module: ModuleSymbol) = {
    if (module.isStatic) {
      globalRuntimeM.reflectModule(module).instance
    } else {
      globalRuntimeM.reflect(model).reflectModule(module).instance
    }
  }

  def applyGetMethodConstant(model: Any, method: MethodSymbol) = {
    val instanceM = globalRuntimeM.reflect(model)
    val methodM = instanceM.reflectMethod(method)

    val key1 = methodM.apply()
    val key2 = methodM.apply()

    if (key1 == key2) {
      Some(key1)
    } else {
      None
    }

  }

}