package com.github.rehei.scala.dox.model.table

class DoxTableMatrix(protected val model: DoxTable[_]) {

  def columnCount() = {
    columns().size
  }

  def columns() = {
    model.root.leavesRecursive().map(_.config)
  }

  def data() = {
    model.data()
  }

  def head() = {
    model.head()
  }

}