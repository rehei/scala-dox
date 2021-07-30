package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

object DoxTableLatex  {

  def makeItSo(doxTree: DoxNode) = {
    """\begin{table}
        \centering;
        \begin{tabularx}{\textwidth } {""" + doxTree.nodeChildren.flatMap(_.leaves()).map(_ => "c").mkString(" ") + """} {
        \toprule
        """ +
      getHeaders(doxTree) +
      """
        \midrule
          \bottomrule
       
         \end{tabularx}
\end{table}
      """
  }

  def getHeaders(doxTree: DoxNode) = {
    println(doxTree.treeRows())
//
//    (for (row <- doxTree.treeRows()) yield {
//      row.map(entry => columnHeader(entry)).mkString(" & ")
//    }).mkString("\\\\ \n") + "\\\\ "
  }

  protected def columnHeader(entry: DoxTreeItem) = {
    if (entry.leaves().length <= 1) {
      entry.baseLabel
    } else {
      "\\multicolumn{" + entry.leaves().length + "}{c}{" + entry.baseLabel + "}"
    }
  }
}