package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.ex.DoxBibKeyFinalException
import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.util.ReflectUtils

case class DoxBibKeyScanner(any: AnyRef) {

  protected lazy val result: Seq[DoxBibKey] = {
    list(any)
  }

  def list() = {
    result
  }

  protected def list(model: Any): Seq[DoxBibKey] = {

    val instance = ReflectUtils.reflectInstance(model)

    instance.symbol.typeSignature.members.flatMap {

      declaration =>

        {
          declaration match {
            case m: ModuleSymbol => traverseModule(model, m)
            case m: MethodSymbol if ReflectUtils.isSetter[DoxBibKey](m) => traverseSetter(m)
            case m: MethodSymbol if ReflectUtils.isGetter[DoxBibKey](m) => traverseGetter(model, m)
            case _ => Seq.empty
          }
        }

    }.toSeq

  }

  protected def traverseModule(model: Any, module: ModuleSymbol) = {
    list(ReflectUtils.applyModule(model, module))
  }

  protected def traverseGetter(model: Any, method: MethodSymbol) = {
    ReflectUtils.applyGetMethodConstant(model, method).toSeq.map(_.asInstanceOf[DoxBibKey])
  }

  protected def traverseSetter(method: MethodSymbol) = {
    val keyname = method.fullName.stripSuffix("_$eq")

    throw new DoxBibKeyFinalException("The key " + keyname + " must not be variable.")
  }

}