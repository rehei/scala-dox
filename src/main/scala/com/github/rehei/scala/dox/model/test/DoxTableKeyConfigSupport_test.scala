package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyCategory
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion

class DoxTableKeyConfigSupport_test(val conversion: DoxTableStringConversion) {

  protected object DataTableKeyConfigBuilder {
    def name(in: String) = new {
      def alignment(select: DoxTableAlignment.type => DoxTableAlignment) = new {
        def dynamic(isDynamic: Boolean) = {
          new DoxTableKeyConfig_test(in, select(DoxTableAlignment), isDynamic, conversion, None, None,None) {
            def category(category: DoxTableKeyCategory) = {
              this.copy(categoryOption = Some(category))
            }
          }
        }
      }
    }
  }

  def apply(callback: DataTableKeyConfigBuilder.type => DoxTableKeyConfig_test) = {
    callback(DataTableKeyConfigBuilder)
  }

}