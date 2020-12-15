package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxItem

case class DoxBuilderList(args: Seq[DoxItem]) {
  
  def item(in: String) = {
    this.copy(args = args :+ DoxItem(in))
  }
  
}
