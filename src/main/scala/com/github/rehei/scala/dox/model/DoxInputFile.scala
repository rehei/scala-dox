package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import java.nio.file.Path

case class DoxInputFile(reference: DoxInputReference, target: DoxInputTarget) {

  def update(filename: String) = {
    this.copy(target = target.update(filename))
  }

}