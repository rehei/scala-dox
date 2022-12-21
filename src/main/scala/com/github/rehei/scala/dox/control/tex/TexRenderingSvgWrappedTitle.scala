package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleSvgConfig

case class TexRenderingSvgWrappedTitle(include: String, config: DoxHandleSvgConfig, title: String) extends TexRenderingSvgWrapped {

  import tmpMarkup._

  protected val tableTotalSize = config.scale + "\\textwidth"
  protected val columnAlignment = "l"

  protected override def generateInternal() = {
    \ vspace { "4pt" };
    \ centering;
    appendTitle()
    \ includegraphics { include }
    appendBottom()
  }

  protected def appendTitle() {
    $ { _ tabular$ & { (tableTotalSize) } { columnAlignment } } {
      \ toprule;
      \ plain { title + "\\\\" }
      \ midrule;
    }
  }

  protected def appendBottom() {
    $ { _ tabular$ & { (tableTotalSize) } { columnAlignment } } {
      \ bottomrule;
    }
  }
}