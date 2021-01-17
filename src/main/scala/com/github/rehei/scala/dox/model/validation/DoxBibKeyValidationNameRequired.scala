package com.github.rehei.scala.dox.model.validation

import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import org.apache.commons.lang3.StringUtils

class DoxBibKeyValidationNameRequired extends IDoxBibKeyValidation {

  def validate(key: DoxBibKey) {
    if (StringUtils.isBlank(key.name)) {
      throw new DoxBibKeyValueBlankException("Name of key should not be blank.")
    }
  }

}