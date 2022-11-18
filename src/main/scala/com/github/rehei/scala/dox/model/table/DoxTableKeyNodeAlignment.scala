package com.github.rehei.scala.dox.model.table

import java.text.DecimalFormat

object DoxTableKeyNodeAlignment {

  object AlignLeft extends DoxTableKeyNodeAlignment {
    override def texAlignmentMinipage(text: String) = {
      environment("flushleft", text)
    }
    override def texAlignment(size: Double) = {
      slashed("""\raggedright""", size)
    }
    override def texAlignmentColumnhead() = {
      "l"
    }
  }

  object AlignRight extends DoxTableKeyNodeAlignment {
    override def texAlignmentMinipage(text: String) = {
      environment("flushright", text)
    }
    override def texAlignment(size: Double) = {
      slashed("""\raggedleft""", size)
    }
    override def texAlignmentColumnhead() = {
      "r"
    }
  }

  object AlignCenter extends DoxTableKeyNodeAlignment {
    override def texAlignmentMinipage(text: String) = {
      environment("center", text)
    }
    override def texAlignment(size: Double) = {
      slashed("""\centering""", size)
    }
    override def texAlignmentColumnhead() = {
      "c"
    }
  }

  object AlignNumeric extends DoxTableKeyNodeAlignment {
    override def texAlignmentMinipage(text: String) = {
      AlignCenter.texAlignmentMinipage(text)
    }
    override def texAlignment(size: Double) = {
      "S[table-number-alignment=center, table-column-width=" + size + "cm]"
    }
    override def texAlignmentColumnhead() = {
      AlignCenter.texAlignmentColumnhead()
    }
  }

}

abstract class DoxTableKeyNodeAlignment {

  def texAlignmentMinipage(text: String): String
  def texAlignment(size: Double): String
  def texAlignmentColumnhead(): String

  protected def slashed(command: String, size: Double) = {
    ">{" + command + """\arraybackslash""" + "}" + sizeString(size)
  }

  protected def sizeString(size: Double) = {
    val df = new DecimalFormat("#")
    df.setMinimumIntegerDigits(1)
    df.setMaximumFractionDigits(3)
    val stringValue = df.format(size)

    "p{" + stringValue + "cm}"
  }

  protected def environment(name: String, content: String) = {
    s"""\\begin{${name}}${content}\\end{${name}}"""
  }

}
  
