package com.github.rehei.scala.dox.model.table.content

import com.github.rehei.scala.dox.text.TextAST

object DoxContent {

  sealed case class DoxValue[+T <: AnyRef](value: T) extends DoxOption[T]
  case object DoxSpace extends DoxOption
  case object DoxRule extends DoxOption
  case class DoxLegend(heading: String, content: Seq[TextAST]) extends DoxOption

  sealed abstract class DoxOption[+T <: AnyRef]

}