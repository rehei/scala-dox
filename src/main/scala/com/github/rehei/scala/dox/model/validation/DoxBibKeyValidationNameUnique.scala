package com.github.rehei.scala.dox.model.validation

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameUniqueException

case class DoxBibKeyValidationNameUnique() extends IDoxBibKeyValidation {

  protected val map = scala.collection.mutable.Map[String, DoxBibKey]()

  def validate(in: DoxBibKey) {

    if (map.contains(in.name)) {
      throw new DoxBibKeyNameUniqueException("Unique name for BibKey is required")
    } else {
      map.put(in.name, in)
    }

  }

}