package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.reference.ReferenceKey
import com.github.rehei.scala.dox.RenderingBase
import com.github.rehei.scala.dox.model.DoxDelegate
import com.github.rehei.scala.dox.model.DoxReference

case class DoxTextBuilder(base: RenderingBase, args: Seq[DoxDelegate]) {
  
  def text(in: String) = {
    copyAppend(DoxDelegate(() => base.text(in)))
  }
  def ref(reference: DoxReference) = {
    copyAppend(DoxDelegate(() => base.ref(reference)))
  }
  def citet(reference: ReferenceKey) = {
    copyAppend(DoxDelegate(() => base.citet(reference)))
  }
  def citep(reference: ReferenceKey) = {
    copyAppend(DoxDelegate(() => base.citep(reference)))
  }
  def cite(reference: ReferenceKey) = {
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