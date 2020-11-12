package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.DoxDelegate
import com.github.rehei.scala.dox.model.DoxReference

case class DoxBuilderText(base: DoxRenderingBase, args: Seq[DoxDelegate]) {
  
  def text(in: String) = {
    copyAppend(DoxDelegate(() => base.text(in)))
  }
  def ref(reference: DoxReference) = {
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