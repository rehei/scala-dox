package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException
import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.util.ReflectUtils

case class DoxBibKeyScanner(any: AnyRef) {

  protected val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

  protected lazy val result: Seq[DoxBibKey] = {
    list(any)
  }

  def list() = {
    result
  }

  protected def list(model: Any): Seq[DoxBibKey] = {

    val instance = globalRuntimeM.reflect(model)

    instance.symbol.typeSignature.members.flatMap {

      declaration =>

        println(declaration)

        {
          declaration match {
            case m: ModuleSymbol => traverseModule(m, model)
            case m: MethodSymbol if ReflectUtils.isSetter[DoxBibKey](m) => traverseSetter(m)
            case m: MethodSymbol if ReflectUtils.isGetter[DoxBibKey](m) => traverseGetter(m, model)
            case _ => Seq.empty
          }
        }

    }.toSeq

  }

  protected def traverseModule(module: ModuleSymbol, model: Any) = {

    println("traverse module" + module)

    val subInstance = {
      if (module.isStatic) {
        globalRuntimeM.reflectModule(module).instance
      } else {
        globalRuntimeM.reflect(model).reflectModule(module).instance
      }
    }


    list(subInstance)

  }

  protected def traverseGetter(method: MethodSymbol, instance: Any) = {
    val instanceM = globalRuntimeM.reflect(instance)
    val methodM = instanceM.reflectMethod(method)
    val key1 = methodM.apply().asInstanceOf[DoxBibKey]
    val key2 = methodM.apply().asInstanceOf[DoxBibKey]

    if (key1 == key2) {
      Seq(key1)
    } else {
      Seq.empty
    }

  }

  protected def traverseSetter(method: MethodSymbol) = {
    val keyname = method.fullName.stripSuffix("_$eq")

    throw new DoxBibKeyNotFinalException("The key " + keyname + " must not be variable.")
  }

}