package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.model.ex.DoxBibKeyObjectRequiredException

class DoxBibKeyScanner() {

  import scala.reflect.runtime.universe._

  protected val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

  def list[T](implicit typeTag: TypeTag[T]): Seq[DoxBibKey] = {

    val symbol = typeTag.tpe.termSymbol

    assertStaticModule(symbol)

    val instance = globalRuntimeM.reflectModule(symbol.asModule).instance

    list(symbol.typeSignature, instance)
  }

  protected def assertStaticModule(symbol: Symbol) = {
    val isStaticModule = symbol.isModule && symbol.isStatic
    if (!isStaticModule) {
      throw new DoxBibKeyObjectRequiredException("Source for key scanning has to be a scala object.")
    }
  }

  protected def list(source: Type, instance: Any): Seq[DoxBibKey] = {

    val stack = ArrayBuffer[DoxBibKey]()

    val instanceM = globalRuntimeM.reflect(instance)

    for (declaration <- source.members) {

      if (declaration.isModule) {

        val subInstance = {
          if (declaration.isStatic) {
            globalRuntimeM.reflectModule(declaration.asModule).instance
          } else {
            instanceM.reflectModule(declaration.asModule).instance
          }
        }
        stack.appendAll(list(declaration.asModule.typeSignature, subInstance))
      }

      if (declaration.isMethod && declaration.asMethod.isGetter && declaration.asMethod.returnType <:< typeOf[DoxBibKey]) {

        val key = instanceM.reflectMethod(declaration.asMethod).apply().asInstanceOf[DoxBibKey]
        
        key.validate()
        
        stack.append(key)
      }
    }

    stack

  }

}