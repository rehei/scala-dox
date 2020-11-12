package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException

trait DoxBibKeyEnum extends Enumeration {

  abstract class KeyBase extends Val with DoxBibKey {
    protected val clazz = DoxBibKeyEnum.this.getClass()

    override def toString() = {
      "@" + this.getClass.getSimpleName + "{" + super.toString() + "}"
    }

    def name() = {
      validate()
      
      friendlyClassName() + "-" + enumerationValueName() + "-" + caseExtension()
    }

    def validate() {
      if (!isValid()) {
        throw new DoxBibKeyNotValidException("Invalid enum reference in " + clazz.getName)
      }
    }

    protected def isValid() = {
      !enumerationValueName().contains("Invalid enum")
    }

    protected def caseExtension() = {
      val sb = new StringBuilder()
      for (character <- enumerationValueName()) {
        character match {
          case c if c.isUpper => sb.append("U")
          case c if c.isLower => sb.append("L")
          case _              => sb.append("-")
        }
      }
      sb.toString()
    }

    protected def enumerationValueName() = {
      super.toString()
    }

    protected def friendlyClassName() = {
      clazz.getName.replace("$", "-").replace(".", "-")
    }

  }

  case class KeyDOI(_doi: String, year: Long, by: String, title: String) extends KeyBase {
    def lookup() = {
      new DoxBibKeyLookupDoi(this.name(), _doi, year, by, title)
    }
  }

  case class KeyRAW(_raw: String) extends KeyBase {
    def lookup() = {
      new DoxBibKeyLookupRaw(this.name(), _raw)
    }
  }

  protected def fromDOI(doi: String) = {
    new {
      def year(year: Long) = new {
        def by(by: String) = new {
          def title(title: String) = {
            KeyDOI(doi, year, by, title)
          }
        }
      }
    }
  }

  protected def fromRAW(content: String) = {
    KeyRAW(content)
  }

}