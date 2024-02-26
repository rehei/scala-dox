package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxViewModelTablePlain
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import com.github.rehei.scala.dox.util.SerializeUtils

case class DoxHandleTablePlain(target: DoxTarget) {

  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-table")

  def handle(view: DoxViewModelTablePlain) = {
    val file = DoxInputData(resolve.transform(view.label), view.content)
    serialize.write(file)
  }

}