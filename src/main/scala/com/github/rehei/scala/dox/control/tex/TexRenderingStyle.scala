package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode

object TexRenderingStyle {

  object NONE extends TexRenderingStyle {
    override def get(width: String, text: String) = {
      text
    }
    override def texAlignmentMinipage(node: DoxTableKeyNode, text: String) = {
      text
    }
  }

  object MINIPAGE extends TexRenderingStyle {
    override def get(width: String, text: String) = {
      "\\columnBox{" + width + "}{" + text + "}"
    }
  }

  object MINIPAGE_FRAMED extends TexRenderingStyle {
    def get(width: String, text: String) = {
      "\\columnBoxFramed{" + width + "}{" + text + "}"
    }
  }

}

abstract class TexRenderingStyle {
  
  def get(width: String, text: String): String

  def texAlignmentHeadWithSize(node: DoxTableKeyNode) = {
    val size = node.dimension().width
    node.format.alignment.texAlignmentHeadWithSize(size)
  }
  
  def texAlignmentHeadShort(node: DoxTableKeyNode) = {
    node.format.alignment.texAlignmentHeadShort()
  }
  
  def texAlignmentMinipage(node: DoxTableKeyNode, text: String) = {
    node.format.alignment.texAlignmentMinipage(text)
  }
  
}

