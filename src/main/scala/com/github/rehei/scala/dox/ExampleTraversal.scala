package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import scala.collection.mutable.ArrayBuffer

object ExampleTraversal {

  import scala.reflect.runtime.universe._

  class DoxBibKeyRepository() {

    protected val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

    def list(source: Type): Seq[String] = {

      val symbol = source.termSymbol

      val stack = ArrayBuffer[String]()

      if (symbol.isModule) {
        val instance = globalRuntimeM.reflectModule(symbol.asModule).instance
        stack.appendAll(list(source.termSymbol.typeSignature, instance))
      }

      if (symbol.isClass) {
        val instance = Class.forName(source.typeSymbol.fullName).newInstance()
        stack.appendAll(list(source.termSymbol.typeSignature, instance))
      }

      stack
    }

    protected def list(source: Type, instance: Any): Seq[String] = {

      val stack = ArrayBuffer[String]()

      val instanceM = globalRuntimeM.reflect(instance)

      for (declaration <- source.decls) {

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

        if (declaration.isClass) {
          val subInstance = Class.forName(declaration.fullName).newInstance()
          stack.appendAll(list(declaration.asClass.typeSignature, subInstance))
        }

        if (declaration.isMethod && declaration.asMethod.isGetter) {

          val isBibKey = { declaration.asMethod.returnType <:< (typeOf[DoxBibKey]) }
          val key = instanceM.reflectMethod(declaration.asMethod).apply()

          stack.append("IS BIB KEY " + key)
        }
      }

      stack

    }

  }

  def main(args: Array[String]): Unit = {

    val result = new DoxBibKeyRepository().list(typeOf[ExampleReference.type])

    for (key <- result) {
      println(key)
    }

  }

}