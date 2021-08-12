package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import com.github.rehei.scala.macros.Query

case class DoxTableFactory[T <: AnyRef](
  callbackConfig: DoxTableConfigBuilder.type => DoxTableConfig,
  callbackSeq:    (DoxTableFactoryKeySelection[T] => DoxTableFactoryKey)*)(implicit clazzTag: ClassTag[T]) {

  implicit class DoxTableKeyNodeExt(base: DoxTableKeyNode) {
    def query(query: Query[_]) = {
      DoxTableKeyNode(DoxTableKeyNodeType.key(query), base.config, Seq.empty)
    }
  }

  protected val query = new Query[T]()

  protected val keys = {
    callbackSeq.map(callback => callback(DoxTableFactoryKeySelection(query)))
  }

  protected val config = callbackConfig(DoxTableConfigBuilder)

  def get() = {

    val factory = DoxTableKeyNodeFactory[T]()(clazzTag)
    import factory._

    val head = {
      Root(config.caption).appendAll(keys.map(m => Node(m.config).query(m.query)))
    }

    DoxTable[T](head)(clazzTag)
  }

}