package com.github.rehei.scala.dox.model.table

case class DoxTable(
  protected val _config: DoxTableConfig,
  protected val _head:   Seq[DoxTableKeyConfig],
  protected val _data:   Seq[Seq[String]]) {

  protected var index: Option[DoxTableKeyConfig] = None

  def caption() = {
    _config.caption
  }
  
  def withIndex(indexConfig: Option[DoxTableKeyConfig]) = {
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
    index.map {
      config => config +: _head
    } getOrElse {
      _head
    }
  }

}