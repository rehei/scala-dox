package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableStringConversion

case class TestDoxTableStringConversion(shortDate: Boolean) extends DoxTableStringConversion {

  def render(model: Any): String = {

    if (model == null) {
      ""
    } else {

      model match {
        case None                                      => ""
        case Some(input)                               => input.toString()
        case m: Iterable[_] => m.map(render(_)).mkString(", ")
        case m              => m.toString()
      }
    }
  }

}
  
