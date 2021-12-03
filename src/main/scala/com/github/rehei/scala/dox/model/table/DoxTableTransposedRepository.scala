package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectSubscript
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.text.TextFactory
import scala.collection.mutable.Queue

class DoxTableTransposedRepository(root: DoxTableKeyNode, data: ArrayBuffer[Seq[TextAST]], val title: TextAST) {
  case class DoxTableTransposedRow(head: TextAST, data: Seq[TextAST], columnDepth: Int)
  protected val effectiveRoot = root.withNoneTitleChildrenOnly

  def list() = {
    transposedStart()
  }

  protected def transposedStart() = {
    transposedRowsInner(effectiveRoot.children, data.transpose.to[Queue], -1)
  }

  protected def transposedRows(node: DoxTableKeyNode, dataBuffer: Queue[ArrayBuffer[TextAST]], transposed: Seq[DoxTableTransposedRow], parentLevel: Int): Seq[DoxTableTransposedRow] = {
    val currentLevel = parentLevel + 1
    if (node.isLeaf) {
      node.nodeType match {
        case DoxTableKeyNodeType.TITLE       => Seq()
        case DoxTableKeyNodeType.COLUMNSPACE => Seq()
        case other                           => Seq(DoxTableTransposedRow(node.config.base.text, dataBuffer.dequeue, currentLevel))
      }
    } else {
      val currentRow = Seq(DoxTableTransposedRow(node.config.base.text, Seq(), currentLevel))
      val tailRows = transposedRowsInner(node.children, dataBuffer, currentLevel)
      currentRow ++ tailRows
    }
  }

  protected def transposedRowsInner(children: Seq[DoxTableKeyNode], dataBuffer: Queue[ArrayBuffer[TextAST]], currentLevel: Int) = {
    (for (child <- children) yield {
      val minLength = Seq(1, child.leavesRecursive().length).max
      val childData = (1 to minLength).map(_ => dataBuffer.dequeue).to[Queue]
      transposedRows(child, childData, Seq(), currentLevel)

    }).flatten
  }

}