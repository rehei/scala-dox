package com.github.rehei.scala.dox.model.table

object DoxTableStyle {

  def apply() = new DoxTableStyle(false, false, false)
  val NONE = DoxTableStyle.apply()
}

case class DoxTableStyle protected (protected val _isBold: Boolean, protected val _isItalic: Boolean, protected val _isUnderlined: Boolean) {

  def ITALIC = {
    this.copy(_isItalic = true)
  }

  def BOLD = {
    this.copy(_isBold = true)
  }

  def UNDERLINED = {
    this.copy(_isUnderlined = true)
  }

  def applyStyle(text: String) = {

    val tmpBold = {
      if (_isBold) {
        "\\textbf{" + text + "}"
      } else {
        text
      }
    }

    val tmpItalic = {
      if (_isItalic) {
        "\\textit{" + tmpBold + "}"
      } else {
        tmpBold
      }
    }

    if (_isUnderlined) {
      "\\underline{" + tmpItalic + "}"
    } else {
      tmpItalic
    }
  }

}