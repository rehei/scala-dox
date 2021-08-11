package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.model.table.tree.DoxTableTree
import com.github.rehei.scala.dox.model.tree.DoxNodeFactory

case class DoxTableFactory[T <: AnyRef](
    callbackConfig: DoxTableConfigBuilder.type => DoxTableConfig, 
    callbackSeq: (DoxTableFactoryKeySelection[T] => DoxTableFactoryKey)*)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()

  protected val keys = {
    callbackSeq.map(callback => callback(DoxTableFactoryKeySelection(query)))
  }

  protected val config = callbackConfig(DoxTableConfigBuilder)
  
  def get() = {
    
    import DoxNodeFactory._
    
    val head = {
      Root(config.caption).appendAll(keys.map(m => Node(m.config).finalize(m.query)))
    }
    
    DoxTableTree[T](head)
  }

}