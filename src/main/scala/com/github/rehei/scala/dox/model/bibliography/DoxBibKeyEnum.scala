package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxValueDOI
import com.github.rehei.scala.dox.model.DoxValueRAW
import com.github.rehei.scala.dox.util.DoxIndexedRepository

trait DoxBibKeyEnum extends DoxIndexedRepository {

  abstract class KeyBase extends DoxIndexedHandle with DoxBibKey {

    def documentID: Option[DoxValueDOI]

    override def name = {
      friendlyEnumerationValueName() + "-" + friendlyCaseExtension()
    }

     protected def friendlyCaseExtension() = {
      val sb = new StringBuilder()
      for (character <- friendlyEnumerationValueName().split("-").last) {
        character match {
          case c if c.isUpper => sb.append("U")
          case c if c.isLower => sb.append("L")
          case _              => sb.append("-")
        }
      }
      sb.toString()

    }

    protected def friendlyEnumerationValueName() = {
      friendlyString(enumerationValueName())
    }

    protected def enumerationValueName() = {
      super.name
    }

    protected def friendlyString(input: String) = {
      if (input == null || input == "") {
        input
      } else {
        input
          .replace("&", "and")
          .replace(" ", "-")
          .replace("(", "-")
          .replace(")", "-")
          .replace("[", "-")
          .replace("]", "-")
          .replace("\"", "-")
          .replace("ß", "ss")
          .replace("Ö", "OE")
          .replace("Ü", "UE")
          .replace("Ä", "AE")
          .replace("ö", "oe")
          .replace("ü", "ue")
          .replace("ä", "ae")
          .replaceAll("[,?!.:;$\\/+]", "-")
          .replaceAll("-+", "-")
          .replaceAll("-$", "-")
          .replaceAll("^-", "")
      }
    }

  }

  case class KeyDOI(_doi: DoxValueDOI, year: Long, by: String, title: String) extends KeyBase {

    def documentID() = {
      Some(_doi)
    }

    def lookup() = {
      new DoxBibKeyLookupDOI(this.name, _doi, Some(year), Some(by), Some(title))
    }
  }

  case class KeyRAW(_raw: String) extends KeyBase {
    def documentID() = {
      None
    }
    def lookup() = {
      new DoxBibKeyLookupRAW(this.name, new DoxValueRAW(_raw))
    }
  }

  protected def fromDOI(doi: String) = {
    new {
      def year(year: Long) = new {
        def by(by: String) = new {
          def title(title: String) = {
            KeyDOI(new DoxValueDOI(doi), year, by, title)
          }
        }
      }
    }
  }

  protected def fromRAW(content: String) = {
    KeyRAW(content)
  }

}