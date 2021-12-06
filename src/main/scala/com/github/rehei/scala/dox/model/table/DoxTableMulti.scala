package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableMulti(protected val models: Seq[DoxTable[_]]) {
  protected val DEFAULT_WIDTH = 2.0

  val columnsMaxAmount = models.map(_.root.leavesAmount()).max
  val columnsMaxWidths = models.map(_.root.leavesWidths(DEFAULT_WIDTH)).maxBy(_.sum)

  val isMulti = models.length > 1

  val title = {
    models
      .headOption
      .map(titleRoot => Some(titleRoot.root.config.base.text))
      .getOrElse(None)
  }

  def contentHead() = {
    content.headOption.map(m => m).getOrElse(throw new IllegalArgumentException("Missing content Table"))
  }
  def content() = {
    if (isMulti) {
      models.drop(1)
    } else {
      models
    }
  }

}