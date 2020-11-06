package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.reference.DoxBibKeyEnum

object ExampleReference extends DoxBibKeyEnum {

  val PLAIN = {
    fromRAW {
      """
      @book{tietze2003,
        title={Strategische Positionierung in der Automobilbranche: der Einsatz von virtueller Produktentwicklung und Wertsch{\"o}pfungsnetzwerken},
        author={Tietze, Oliver},
        year={2003},
        publisher={Springer-Verlag}
      }
    """
    }
  }

  val MUELLER = {
    fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
      .year(2019).by("Müller, R., Oehm, L.").title("Process industries versus discrete processing: how system characteristics affect operator tasks. ")
  }

  def main(args: Array[String]): Unit = {

    println(PLAIN.lookup().resolve())
    println(MUELLER.lookup().resolve())

  }

}