package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxItem
import com.github.rehei.scala.dox.RenderingBase

case class DoxListBuilder(base: RenderingBase, args: Seq[DoxItem]) {
  
  def item(in: String) = {
    this.copy(args = args :+ DoxItem(in))
  }
  
  def flush() {
    base.list(args.map(_.in))
  }
  
}
