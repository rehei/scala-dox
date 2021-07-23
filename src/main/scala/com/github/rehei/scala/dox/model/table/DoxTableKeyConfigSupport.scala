package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

class DoxTableKeyConfigSupport(val conversion: DoxTableStringConversion) {

  protected object DataTableKeyConfigBuilder {
    def name(in: String) = new {
      def alignment(select: DoxTableAlignment.type => DoxTableAlignment) = new {
        def dynamic(isDynamic: Boolean) = {
          new DoxTableKeyConfig(TextFactory.text(in), select(DoxTableAlignment), isDynamic, conversion, None) {
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