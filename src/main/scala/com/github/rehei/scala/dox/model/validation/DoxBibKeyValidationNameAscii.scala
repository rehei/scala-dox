package com.github.rehei.scala.dox.model.validation

import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameAsciiException
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

class DoxBibKeyValidationNameAscii extends IDoxBibKeyValidation {

  def validate(key: DoxBibKey) {
    if (!StringUtils.isAsciiPrintable(key.name)) {
      throw new DoxBibKeyNameAsciiException("Name should be given in ascii")
    }
  }

}