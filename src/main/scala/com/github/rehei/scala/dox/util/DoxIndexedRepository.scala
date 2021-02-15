package com.github.rehei.scala.dox.util

import scala.collection.mutable
import scala.collection.Seq
import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFoundException

abstract class DoxIndexedRepository {

  class DoxIndexedHandle() {

    val index = nextID.nextID()

    push(this)

    def name() = {
      populateMap()
      get(index)
    }

    override def equals(that: Any): Boolean = {
      that match {
        case that: DoxIndexedHandle => this.index == that.index
        case _                      => false
      }
    }

    override def hashCode: Int = {
      index.hashCode()
    }
  }

  private val nextID = NextID(this.getClass.getName)

  private val buffer = mutable.ListBuffer[String]()
  private val map = mutable.Map[String, String]()

  private var refresh = true

  protected def push(any: DoxIndexedHandle) = {
    refresh = true
    buffer.append(any.getClass.getName)
  }

  protected def populateMap() {
    if (refresh) {
      traverse()
      refresh = false
    }
  }

  protected def get(index: String) = {
    populateMap()
    map.get(index).getOrElse(throw new DoxBibKeyNotFoundException("No mapping found for key"))
  }

  protected def traverse() {
    traverseType(this.getClass.getName, this)
  }

  protected def traverseType(prefix: String, model: Any) {
    val instance = ReflectUtils.reflectInstance(model)

    for (member <- instance.symbol.typeSignature.members if member.isPublic) {
      member match {
        case module: ModuleSymbol => traverseModule(prefix, model, module)
        case method: MethodSymbol => traverseMethod(prefix, model, method)
        case _                    => // do nothing
      }
    }
  }

  protected def traverseMethod(prefix: String, model: Any, method: MethodSymbol) {
    
    for (result <- scala.util.Try(ReflectUtils.applyGetMethodConstant(model, method)).toOption.flatten) {
      result match {
        case model: DoxIndexedHandle => traverseMethodValue(prefix, model, method)
        case model                   => traverseMethodRecursive(prefix, model, method)
      }

    }

  }

  protected def traverseModule(prefix: String, model: Any, module: ModuleSymbol) = {
    traverseType(prefix + "." + module.name, ReflectUtils.applyModule(model, module))
  }

  protected def traverseMethodValue(prefix: String, model: DoxIndexedHandle, method: MethodSymbol) {
    map.put(model.index, prefix + "." + method.name)
  }

  protected def traverseMethodRecursive(prefix: String, model: Any, method: MethodSymbol) {
    if (model.getClass().getName.startsWith(this.getClass.getName)) {
      traverseType(prefix + "." + method.name, model)
    }
  }

}