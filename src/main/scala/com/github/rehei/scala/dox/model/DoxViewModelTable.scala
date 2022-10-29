package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRenderingTable
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle

case class DoxViewModelTable[T <: AnyRef](model: DoxTable[T], label: Option[DoxReferencePersistentTable])