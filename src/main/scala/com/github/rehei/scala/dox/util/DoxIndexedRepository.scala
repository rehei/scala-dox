package com.github.rehei.scala.dox.util

import scala.collection.mutable
import scala.collection.Seq
import scala.reflect.runtime.universe._
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException

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
  private val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

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
    map.get(index).getOrElse(throw new DoxBibKeyNotValidException("No mapping found for key"))
  }

  protected def traverse() {
    traverseType(this.getClass.getName, this)
  }

  protected def traverseType(prefix: String, model: Any) {
    val instance = globalRuntimeM.reflect(model)

    for (member <- instance.symbol.typeSignature.members if member.isPublic) {
      member match {
        case m: ModuleSymbol => traverseModule(prefix, m, instance)
        case m: MethodSymbol => traverseMethod(prefix, m, instance)
        case _               => // do nothing
      }
    }
  }

  protected def traverseMethod(prefix: String, method: MethodSymbol, instance: InstanceMirror) = {
    scala.util.Try {

      val result = instance.reflectMethod(method).apply()

      result match {
        case model: DoxIndexedHandle => traverseMethodValue(prefix, method, model)
        case model                   => traverseMethodRecursive(prefix, method, model)
      }

    } getOrElse {
      Seq.empty
    }

  }

  protected def traverseModule(prefix: String, module: ModuleSymbol, instance: InstanceMirror) = {
    val subInstance = {
      if (module.isStatic) {
        globalRuntimeM.reflectModule(module).instance
      } else {
        instance.reflectModule(module).instance
      }
    }

    traverseType(prefix + "." + module.name, subInstance)
  }

  protected def traverseMethodValue(prefix: String, method: MethodSymbol, handle: DoxIndexedHandle) {
    map.put(handle.index, prefix + "." + method.name)
  }

  protected def traverseMethodRecursive(prefix: String, method: MethodSymbol, container: Any) {
    if (container.getClass().getName.startsWith(this.getClass.getName)) {
      traverseType(prefix + "." + method.name, container)
    }
  }

}