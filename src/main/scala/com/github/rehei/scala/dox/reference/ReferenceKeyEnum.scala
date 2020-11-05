package com.github.rehei.scala.dox.reference

trait ReferenceKeyEnum extends Enumeration {

  abstract class KeyBase extends Val with ReferenceKey {
    protected val clazz = ReferenceKeyEnum.this.getClass().getCanonicalName.replace("$", "").replace(".", "-")

    def name() = {
      val keyName = super.toString()

      val extension = {
        for (character <- keyName) yield {
          if (character.isUpper) {
            "U"
          } else {
            if(character.isLower) {
              "L"
            } else {
              "-"
            }
          }
        }
      }

      clazz + "-" + keyName + "-" + extension.mkString
    }
  }

  class KeyDOI(_doi: String) extends KeyBase {
    def lookup() = {
      new ReferenceLookupDoi(this.name(), _doi)
    }
  }

  class KeyRAW(_raw: String) extends KeyBase {
    def lookup() = {
      new ReferenceLookupRaw(this.name(), _raw)
    }
  }

  protected def fromDOI(doi: String) = {
    new KeyDOI(doi)
  }

  protected def fromRAW(content: String) = {
    new KeyRAW(content)
  }

}