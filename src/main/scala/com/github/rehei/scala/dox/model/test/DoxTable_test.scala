package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxTable_test(
  protected val _config: DoxTableConfig,
  protected val _head:   Seq[Seq[DoxTableKeyConfig]],
  protected val _data:   Seq[Seq[String]]) {

  protected var index: Option[DoxTableKeyConfig_test] = None

  def caption() = {
    _config.caption
  }
  
  def withIndex(indexConfig: Option[DoxTableKeyConfig_test]) = {
    if (_config.enableIndexing) {
      index = indexConfig
    }
  }

  def data = {

    index.map {
      config =>
        {
          _data.zipWithIndex.map { case ((data, index)) => (index + 1).toString() +: data }
        }
    } getOrElse {
      _data
    }

  }

  def head = {
//    index.map(_.)
//    index.map {
//      
//      config => config +: _head
//    } getOrElse {
      _head
//    }
  }

}