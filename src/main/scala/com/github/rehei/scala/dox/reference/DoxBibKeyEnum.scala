package com.github.rehei.scala.dox.reference

trait DoxBibKeyEnum extends Enumeration {

  abstract class KeyBase extends Val with DoxBibKey {
    protected val clazz = DoxBibKeyEnum.this.getClass()

    def name() = {
      val keyName = super.toString()

      if (keyName.contains("Invalid enum")) {
        throw new DoxBibKeyInvalidException("Invalid enum reference in " + clazz.getCanonicalName)
      }

      val extension = {
        for (character <- keyName) yield {
          if (character.isUpper) {
            "U"
          } else {
            if (character.isLower) {
              "L"
            } else {
              "-"
            }
          }
        }
      }

      clazz.getName.replace("$", "-").replace(".", "-") + "-" + keyName + "-" + extension.mkString
    }
  }

  class KeyDOI(_doi: String) extends KeyBase {
    def lookup() = {
      new DoxBibKeyLookupDoi(this.name(), _doi)
    }
  }

  class KeyRAW(_raw: String) extends KeyBase {
    def lookup() = {
      new DoxBibKeyLookupRaw(this.name(), _raw)
    }
  }

  protected def fromDOI(doi: String) = {
    new {
      def year(year: Long) = new {
        def by(description: String) = new {
          def title(title: String) = {
            new KeyDOI(doi)
          }
        }
      }
    }
  }

  protected def fromRAW(content: String) = {
    new KeyRAW(content)
  }

}