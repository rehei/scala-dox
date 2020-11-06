package com.github.rehei.scala.dox.model.table

class DoxTableKeyConfigSupport(val conversion: DoxTableStringConversion) {

  protected object DataTableKeyConfigBuilder {
    def name(in: String) = new {
      def alignment(select: DoxTableAlignment.type => DoxTableAlignment) = new {
        def dynamic(isDynamic: Boolean) = {
          new DoxTableKeyConfig(in, select(DoxTableAlignment), isDynamic, conversion, None) {
            def category(category: DoxTableKeyCategory) = {
              this.copy(categoryOption = Some(category))
            }
          }
        }
      }
    }
  }

  def apply(callback: DataTableKeyConfigBuilder.type => DoxTableKeyConfig) = {
    callback(DataTableKeyConfigBuilder)
  }

}