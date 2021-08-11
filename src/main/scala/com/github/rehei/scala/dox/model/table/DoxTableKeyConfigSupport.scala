package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.text.TextAST

object DoxTableKeyConfigSupport {
  val NONE = new DoxTableKeyConfigSupport(DoxTableStringConversion.NONE).apply(_.NONE)
}

class DoxTableKeyConfigSupport(val conversion: DoxTableStringConversion) {

  protected object DataTableKeyConfigBuilder {
    case class BuildingObject(text: TextAST) {
      def alignment(select: DoxTableAlignment.type => DoxTableAlignment) = new {
        def dynamic(isDynamic: Boolean) = {
          DoxTableKeyConfig(text, select(DoxTableAlignment), isDynamic, conversion)
        }
      }
    }

    val NONE = name(TextFactory.NONE).alignment(_.NONE).dynamic(false)
    def name(in: String) = BuildingObject(TextFactory.text(in))

    def name(text: TextAST) = BuildingObject(text)

  }
  def apply(callback: DataTableKeyConfigBuilder.type => DoxTableKeyConfig) = {
    callback(DataTableKeyConfigBuilder)
  }

}