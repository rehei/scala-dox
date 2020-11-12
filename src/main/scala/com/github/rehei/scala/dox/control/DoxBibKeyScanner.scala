package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.model.ex.DoxBibKeySourceObjectRequiredException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

class DoxBibKeyScanner() {

  import scala.reflect.runtime.universe._

  protected val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

  def list[T](implicit typeTag: TypeTag[T]): Seq[DoxBibKey] = {

    val module = getStaticModule(typeTag)
    val instance = globalRuntimeM.reflectModule(module).instance

    list(module.typeSignature, instance)
  }

  protected def getStaticModule[T](typeTag: TypeTag[T]) = {

    val symbol = typeTag.tpe.termSymbol

    val isStaticModule = symbol.isModule && symbol.isStatic
    if (!isStaticModule) {
      throw new DoxBibKeySourceObjectRequiredException("Source for key scanning has to be a scala object.")
    }

    symbol.asModule
  }

  protected def list(source: Type, instance: Any): Seq[DoxBibKey] = {

    source.members.flatMap {

      declaration =>
        {
          declaration match {
            case m: ModuleSymbol if m.isStatic => traverseModule(m, instance)
            case m: MethodSymbol if m.isGetter && m.returnType <:< typeOf[DoxBibKey] => traverseGetter(m, instance)
            case m: MethodSymbol if m.isSetter && m.paramLists(0)(0).typeSignature <:< typeOf[DoxBibKey] => traverseSetter(m)
            case _ => Seq.empty
          }
        }

    }.toSeq

  }

  protected def traverseModule(module: ModuleSymbol, instance: Any) = {

    val subInstance = {
      if (module.isStatic) {
        globalRuntimeM.reflectModule(module).instance
      } else {
        val instanceM = globalRuntimeM.reflect(instance)
        instanceM.reflectModule(module).instance
      }
    }

    list(module.typeSignature, subInstance)
  }

  protected def traverseGetter(method: MethodSymbol, instance: Any) = {
    val instanceM = globalRuntimeM.reflect(instance)
    val key = instanceM.reflectMethod(method).apply().asInstanceOf[DoxBibKey]

    key.validate()

    Seq(key)
  }

  protected def traverseSetter(method: MethodSymbol) = {
    val keyname = method.fullName.stripSuffix("_$eq")

    throw new DoxBibKeyNotFinalException("The key " + keyname + " must not be variable.")

    Seq.empty
  }

}