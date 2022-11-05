package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode

protected case class TexHead(value: DoxTableHeadRowKeyWithOffset, style: TexRenderingStyle) {

  protected val text = {
    Text2TEX(false).generate(value.key.node.textHead())
  }

  protected val expressionText = {

    if (value.key.node.format.isRotated) {
      "\\rotatebox{45}{" + text + " }"
    } else {
      getHeadAlignmentMinipage(value.key.node, text)
    }
  }

  protected val expressionDimension = {

    val tabcolsepCount = {
      (value.key.size - 1) * 2
    }

    "\\dimexpr(\\tabcolsep*" + tabcolsepCount + ")+" + value.key.width + "cm"
  }

  val columnCountOffset = {
    value.offset
  }

  val columnCount = {
    value.key.size
  }

  val content = {
    style.get(expressionDimension, expressionText)
  }

  val content2 = {
    style.get(expressionDimension, text)
  }

  protected def getHeadAlignmentMinipage(node: DoxTableKeyNode, text: String) = {
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.lMinipage(text)
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.rMinipage(text)
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.cMinipage(text)
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.cMinipage(text)
      case _                                => throw new RuntimeException("This should not happen")
    }
  }

}