package com.github.rehei.scala.dox.model.table

trait DataTableKeyConfigSupport {

  object KeyBuilder {
    def name(in: String) = new {
      def alignment(select: DataTableAlignment.type => DataTableAlignment) = new {
        def dynamic(isDynamic: Boolean) = {
          new DataTableKeyConfig(in, select(DataTableAlignment), isDynamic, conversion(), None) {
            def category(category: DataTableKeyCategory) = {
              this.copy(categoryOption = Some(category))
            }
          }
        }
      }
    }
  }

  def config(callback: KeyBuilder.type => DataTableKeyConfig) = {
    callback(KeyBuilder)
  }

  def conversion(): DataTableStringConversion = {
    new DataTableStringConversionDefault()
  }

}