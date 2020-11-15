package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum

object ExampleReference extends DoxBibKeyEnum {

  val PLAIN = {
    fromRAW {
      """
        @inproceedings{wenzel2012mathematical,
          title={Mathematical computations for linked data applications with openmath},
          author={Wenzel, Ken and Reinhardt, Heiner},
          booktitle={Proceedings of the 24th Workshop on OpenMath},
          pages={38--48},
          year={2012}
        }
      """
    }
  }

  object REINHARDT extends DoxBibKeyEnum {

    val REINHARDT_2019 = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }

    val REINHARDT_2020a = {
      fromDOI("https://doi.org/10.1016/j.procir.2020.01.078")
        .year(2020).by("Heiner Reinhardt and Jan-Peter Bergmann and Marc Münnich and David Rein and Matthias Putz").title("A survey on modeling and forecasting the energy consumption in discrete manufacturing")
    }
    val REINHARDT_2020b = {
      fromDOI("https://doi.org/10.1016/j.procir.2020.04.055")
        .year(2020).by("Heiner Reinhardt and Jan-Peter Bergmann and Anke Stoll and Matthias Putz").title("Temporal analysis of event-discrete alarm data for improved manufacturing")
    }

  }

  def main(args: Array[String]): Unit = {

    println(PLAIN.lookup().resolveValidated())
    println(REINHARDT.REINHARDT_2019.lookup().resolveValidated())
    println(REINHARDT.REINHARDT_2020a.lookup().resolveValidated())
    println(REINHARDT.REINHARDT_2020b.lookup().resolveValidated())

  }

}