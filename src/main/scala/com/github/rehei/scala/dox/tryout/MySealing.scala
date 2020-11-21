package com.github.rehei.scala.dox.tryout

import scala.collection.mutable
import scala.reflect.runtime.universe._
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.util.NextID

abstract class MySealing {

  private val globalRuntimeM = runtimeMirror(this.getClass.getClassLoader)

  private val buffer = mutable.ListBuffer[String]()
  private val map = mutable.Map[String, String]()
  
  private var refresh = true
  private val nextID = NextID(this.getClass.getCanonicalName)

  sealed abstract class Handle {

    protected val _index = nextID.nextID()

    refresh = true
    buffer.append(this.getClass.getName)

    def name() = {
      populateMap()
      map.get(_index).get
    }

    def index() = {
      _index
    }
  }

  case class Value(doi: String) extends Handle

  protected def fromDOI(_doi: String) = {
    Value(_doi)
  }

  protected def populateMap() {
    if (refresh) {
      traverse()
      refresh = false
    }
  }

  protected def traverse() {
    traverseType(this.getClass.getName, this)
  }

  protected def traverseType(prefix: String, model: Any) {
    val instance = globalRuntimeM.reflect(model)

    for (member <- instance.symbol.typeSignature.members) {
      member match {
        case m: ModuleSymbol               => traverseModule(prefix, m, instance)
        case m: MethodSymbol if m.isGetter => traverseMethod(prefix, m, instance)
        case _                             => // do nothing
      }
    }
  }

  protected def traverseMethod(prefix: String, method: MethodSymbol, instance: InstanceMirror) = {
    val result = instance.reflectMethod(method).apply()
    if (result.isInstanceOf[Value]) {
      val value = result.asInstanceOf[Value]
      map.put(value.index(), prefix + "." + method.name)
    } else {
      if (result.getClass().getName.startsWith(this.getClass.getName)) {
        traverseType(prefix + "." + method.name, result)
      }
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

    traverseType(prefix, subInstance)

  }

}