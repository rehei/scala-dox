package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

class DoxTableKeyConfigSupport() {

  protected object DataTableKeyConfigBuilder {
    case class BuildingObject(text: TextAST) {
      def alignment(selectAlignment: DoxTableAlignment.type => DoxTableAlignment) = {
        DoxTableKeyConfig(text, selectAlignment(DoxTableAlignment))
      }
    }

    def name(in: String) = BuildingObject(TextFactory.text(in))
    def name(text: TextAST) = BuildingObject(text)

  }
  def apply(callback: DataTableKeyConfigBuilder.type => DoxTableKeyConfig) = {
    callback(DataTableKeyConfigBuilder)
  }

}