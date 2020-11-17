package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.util.NormalizeUtils
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException

class DoxValueRAW(protected val _value: String) {

  val value = NormalizeUtils.trim(_value)

  if (StringUtils.isBlank(value)) {
    throw new DoxBibKeyValueBlankException("Value RAW should not be blank")
  }

}