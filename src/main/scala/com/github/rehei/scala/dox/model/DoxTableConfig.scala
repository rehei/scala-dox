package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.control.tex.ColumnType

object DoxTableConfig {

  val DUMMY = new DoxTableConfigCompute(false)

  def wideTop() = {
    new DoxTableConfigWide("t")
  }

  def wideBottom() = {
    new DoxTableConfigWide("b")
  }

  def computeAndFill() = {
    new DoxTableConfigCompute(true)
  }

  protected class DoxTableConfigWide(position: String) extends DoxTableConfig(position, true, true)

  protected class DoxTableConfigCompute(fill: Boolean) extends DoxTableConfig("h", fill, false)

}

abstract class DoxTableConfig(val position: String, val fill: Boolean, val fullpage: Boolean) {

  def computeWidth(model: DoxTableMatrix) = {
    if (fill) {
      if (fullpage) {
        "\\textwidth"
      } else {
        "\\linewidth"
      }
    } else {
      val totalWidth = model.totalWidth()
      val totalSeparatorCount = model.totalSeparatorCount()
      "\\dimexpr(\\tabcolsep*" + totalSeparatorCount + ")+" + totalWidth + "cm"
    }
  }

  def computeFormatString(model: DoxTableMatrix): String = {

    val prefix = {
      if (fill) {
        "@{\\extracolsep{\\fill}}"
      } else {
        ""
      }
    }

    prefix ++ model.dimension().map(node => getTexAlignment(node)).mkString
  }

  protected def getTexAlignment(node: DoxTableKeyNode) = {
    val size = node.dimension().width
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.l(size)
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.r(size)
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.c(size)
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.numeric(size)
      case _                                => throw new RuntimeException("This should not happen")
    }
  }

}


