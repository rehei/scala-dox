package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectSubscript
import scala.collection.mutable.ArrayBuffer

class DoxTableTransposedRepository(root: DoxTableKeyNode, data: ArrayBuffer[Seq[TextAST]]) {
  case class DoxTableTransposedRow(head: TextAST, data: Seq[TextAST], columnDepth: Int)

  protected val factory = DoxTableKeyNodeFactory()

  implicit class AbstractDoxNodeExt(base: DoxTableKeyNode) {

    def hasNonWhitespaceChildren() = {
      base.children.filterNot(_.nodeType == DoxTableKeyNodeType.WHITESPACE).size > 0
    }

    def byLevel(level: Int): Seq[DoxTableKeyNode] = {

      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }

    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }

    protected def withWhitespaceMax(max: Int): DoxTableKeyNode = {
      if (max > 0) {
        val extension = {
          if (base.children.isEmpty) {
            Seq(factory.Whitespace())
          } else {
            Seq.empty
          }
        }
        base.copy(children = (base.children ++ extension).map(_.withWhitespaceMax(max - 1)))
      } else {
        base.copy()
      }
    }

  }

  def list() = {
    val a = data.transpose
    val b = root
    var dataBuffer = a

    (for (child <- b.children) yield {
      val leavesSize = {
        if (child.leavesRecursive().length > 0) {
          child.leavesRecursive().length
        } else {
          1
        }
      }
      val childrenRows = getTransposedRows(child, dataBuffer.take(leavesSize), Seq(), root.depth)
      dataBuffer = dataBuffer.drop(leavesSize)
      childrenRows
    }).flatten

  }

  protected def getTransposedRows(node: DoxTableKeyNode, dataBuffer: ArrayBuffer[ArrayBuffer[TextAST]], transposed: Seq[DoxTableTransposedRow], maxDepth: Int): Seq[DoxTableTransposedRow] = {

    if (node.isLeaf) {
      Seq(DoxTableTransposedRow(node.config.text, dataBuffer.take(1).headOption.map(m => m).getOrElse(throw new Exception), maxDepth - node.depth()))

    } else {
      var buffer = dataBuffer
      val dataRow = Seq(DoxTableTransposedRow(node.config.text, Seq(), maxDepth - node.depth()))

      val test = (for (child <- node.children) yield {
        val minLength = {
          if (child.leavesRecursive().length > 0) {
            child.leavesRecursive().length
          } else {
            1
          }
        }
        val currentStuff = getTransposedRows(child, buffer.take(minLength), Seq(), maxDepth)
        buffer = buffer.drop(minLength)
        currentStuff

      }).flatten
      dataRow ++ test
    }

  }

}