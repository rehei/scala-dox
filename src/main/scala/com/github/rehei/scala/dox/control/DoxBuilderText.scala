package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.DoxDelegate
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.DoxReferenceLike

case class DoxBuilderText(base: DoxRenderingBase, args: Seq[DoxDelegate]) {




  def nonBreakingSpace = {
    copyAppend(DoxDelegate(() => base.nonBreakingSpace))
  }

  def text(in: String) = {
    copyAppend(DoxDelegate(() => base.textNoSpace(" " + in))) // yes, that is right.
  }

  def textItalic(in: String) = {
    copyAppend(DoxDelegate(() => base.textItalic(" " + in)))
  }

  def textNoSpace(in: String) = {
    copyAppend(DoxDelegate(() => base.textNoSpace(in)))
  }

  def plain(in: String) = {
    copyAppend(DoxDelegate(() => base.plain(in)))
  }

  def ref(reference: DoxReferenceLike) = {
    copyAppend(DoxDelegate(() => base.ref(reference)))
  }
  def citet(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.citet(reference)))
  }
  def citep(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.citep(reference)))
  }
  def cite(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.cite(reference)))
  }



  def flush() {
    for (arg <- args) {
      arg.callback()
    }
  }
  protected def copyAppend(value: DoxDelegate) = {
    this.copy(args = args :+ value)
  }

}