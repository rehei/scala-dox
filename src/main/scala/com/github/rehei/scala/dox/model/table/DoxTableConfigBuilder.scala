package com.github.rehei.scala.dox.model.table

object DoxTableConfigBuilder {
  def caption(caption: String) = new {
    def indexing(enableIndexing: Boolean) = {
      DoxTableConfig(caption, enableIndexing)
    }
  }
}
