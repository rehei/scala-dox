package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectSubscript
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.text.TextFactory
import scala.collection.mutable.Queue

class DoxTableTransposedRepository(root: DoxTableKeyNode, data: ArrayBuffer[Seq[TextAST]]) {
  case class DoxTableTransposedRow(head: TextAST, data: Seq[TextAST], columnDepth: Int)

  implicit class AbstractDoxNodeExt(base: DoxTableKeyNode) {

    val title = {
      root.children.headOption.map(head => head.nodeType match {
        case DoxTableKeyNodeType.TITLE => head.config.text
        case other                     => TextFactory.NONE
      }).getOrElse(TextFactory.NONE)
    }

    protected val noneTitleChildren = {
      base.children.headOption.map(
        head => head.nodeType match {
          case DoxTableKeyNodeType.TITLE => base.children.drop(1)
          case other                     => base.children
        }).getOrElse(base.children)

    }
    def checkValidity() {
      for (child <- noneTitleChildren) {
        findInvalid(child)
      }
    }
    protected def findInvalid(node: DoxTableKeyNode): Unit = {
      checkNode(node)
      for (child <- node.children) {
        findInvalid(child)
      }
    }

    protected def checkNode(node: DoxTableKeyNode) = {
      node.nodeType match {
        case DoxTableKeyNodeType.TITLE => throw new IllegalArgumentException("Title Node found, but not as first root node child.")
        case other                     => true
      }
    }
  }

  root.checkValidity()

  val title = root.title

  def list() = {
    transposedStart()
  }

  protected def transposedStart() = {
    transposedRowsInner(root.children, data.transpose.to[Queue], -1)
  }

  protected def transposedRows(node: DoxTableKeyNode, dataBuffer: Queue[ArrayBuffer[TextAST]], transposed: Seq[DoxTableTransposedRow], parentLevel: Int): Seq[DoxTableTransposedRow] = {
    val currentLevel = parentLevel + 1
    if (node.isLeaf) {
      node.nodeType match {
        case DoxTableKeyNodeType.TITLE => Seq()
        case other                     => Seq(DoxTableTransposedRow(node.config.text, dataBuffer.dequeue, currentLevel))
      }
    } else {
      val currentRow = Seq(DoxTableTransposedRow(node.config.text, Seq(), currentLevel))
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