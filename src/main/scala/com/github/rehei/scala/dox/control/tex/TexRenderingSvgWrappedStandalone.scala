package com.github.rehei.scala.dox.control.tex

case class TexRenderingSvgWrappedStandalone(include: String) extends TexRenderingSvgWrapped {

  import tmpMarkup._

  protected override def generateInternal() {
    \ vspace { "4pt" };
    \ centering;
    \ includegraphics { include }
  }

}