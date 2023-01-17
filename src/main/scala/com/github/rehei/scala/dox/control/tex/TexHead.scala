package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode

case class TexHead(value: DoxTableHeadRowKeyWithOffset, style: TexRenderingStyle) {

  protected val text = {
    Text2TEX(false).generate(value.key.node.textHead())
  }

  protected val expressionText = {

    value.key.node.format.rotateOption.map {
      degree =>
        {
          "\\rotatebox{" + degree + "}{" + text + " }"
        }
    } getOrElse {
      style.texAlignmentMinipage(value.key.node, text)
    }

  }

  protected val expressionDimension = {
    val tabcolsepCount = {
      (value.key.size - 1) * 2
    }

    "\\dimexpr(\\tabcolsep*" + tabcolsepCount + ")+" + value.key.width + "cm"
  }

  val columnAlignmentShort = {
    style.texAlignmentHeadShort(value.key.node)
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

}