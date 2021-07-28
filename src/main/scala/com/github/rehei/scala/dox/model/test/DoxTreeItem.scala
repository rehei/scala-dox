package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue

abstract class DoxTreeItem(val baseLabel: String) {

  def isLeaf() = {
    this match {
      case leaf @ DoxLeaf(_, _) => true
      case node @ DoxNode(_, _) => false
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def getChildren() = {
    this match {
      case leaf @ DoxLeaf(_, _) => throw new Exception("Leaves do not have children")
      case node @ DoxNode(_, _) => node.children
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def getValue() = {
    this match {
      case leaf @ DoxLeaf(_, _) => leaf.value
      case node @ DoxNode(_, _) => throw new Exception("Nodes do not have values")
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def getLeaves() = {
    this match {
      case leaf @ DoxLeaf(_, _) => Seq(leaf)
      case node @ DoxNode(_, _) => node.leafChildren()
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  //  def getSubTreeDepth() = {
  //    this match {
  //      case leaf @ DoxLeaf(_, _) => throw new Exception("Only Nodes have a subtree")
  //      case node @ DoxNode(_, _) => node.subTreeDepth()
  //      case _                    => throw new Exception("Neither Node nor Leaf")
  //    }
  //  }
  //  override def toString() = {
  //    this match {
  //      case leaf @ DoxLeaf(_, _) => "Leaf:" + leaf.label + ", " + leaf.value
  //      case node @ DoxNode(_, _) => "Node:" + node.label + "," + node.children.map(_.toString())
  //      case _                    => throw new Exception("Neither Node nor Leaf")
  //    }
  //  }
  //  protected def setParent(parent:DoxTreeItem) = {
  //
  //  }
}

case class DoxLeaf(label: String, value: String) extends DoxTreeItem(label) {

}
case class DoxNode(label: String, children: Seq[DoxTreeItem]) extends DoxTreeItem(label) {
  def leafChildren(treeItems: Seq[DoxTreeItem] = children): Seq[DoxTreeItem] = {
    if (treeItems.isEmpty) {
      Seq.empty
    } else {
      if (!treeItems.head.isLeaf()) {
        leafChildren(treeItems.head.getChildren()) ++ leafChildren(treeItems.drop(1))
      } else {
        treeItems.takeWhile(_.isLeaf()) ++ leafChildren(treeItems.dropWhile(_.isLeaf()))
      }
    }
  }

  //  def subTreeDepth(treeItems: Seq[DoxTreeItem] = children, counter: Int = 0): Int = {
  //    if (treeItems.isEmpty) {
  //      counter
  //    } else {
  //      subTreeDepth(treeItems.dropWhile(_.isLeaf()), counter + 1)
  //    }
  //  }
}

object MakeDoxTree {

  def treeHead(label: String) = new MakeDoxTree {
    //    val asd = withNode("peter").andSubItems(Seq(DoxLeaf("bla", "test"), DoxNode("asd", Seq.empty), DoxLeaf("lis", "glsd")))

  }
}

abstract class MakeDoxTree() {

  val doxTreeHeadSeq = ListBuffer[DoxTreeItem]()

  val instance = this

  def addLeaf(label: String, value: String) = {
    doxTreeHeadSeq.append(DoxLeaf(label, value))
    instance
  }

  //TODO: force addChildren call
  def addNode(label: String) = new {
    val currentNode = DoxNode(label, Seq.empty)
    def addChildren(callback: MakeDoxTree => MakeDoxTree) = {
      val nodeChildren = callback(MakeDoxTree.treeHead(label)).doxTreeHeadSeq
      if (nodeChildren.isEmpty) {
        throw new Exception("Node has to have Children")
      }
      doxTreeHeadSeq.append(currentNode.copy(children = nodeChildren))
      instance
    }
  }

  def leaves() = {
    doxTreeHeadSeq.flatMap(_.getLeaves())
  }

}

object MakeSomeLatex {

  def makeItSo(doxTree: MakeDoxTree) = {
    val compareList = ListBuffer[DoxTreeItem]()
    val leavesList = doxTree.doxTreeHeadSeq.map(_.getLeaves())
    """\begin{table}
        \centering;
        \begin{tabularx}{\textwidth } {""" + doxTree.leaves().map(_ => "c").mkString(" ") + """} {
        \toprule
        \midrule
        """ + getRows(doxTree.doxTreeHeadSeq).mkString("\n") +
      """
          \bottomrule
        }
      }
      """
  }

  def getRows(doxTree: Seq[DoxTreeItem], addedLeavesList: ListBuffer[DoxTreeItem] = ListBuffer.empty): Seq[String] = {
    val nextRow = ListBuffer[DoxTreeItem]()
    //    println("---")
    //    println(doxTree.flatMap(_.getLeaves()))
    //    println(addedLeavesList.flatMap(_.getLeaves()))
    //    println(doxTree)
    //    println(addedLeavesList)
    if (doxTree.isEmpty || (doxTree.flatMap(_.getLeaves()).length == addedLeavesList.flatMap(_.getLeaves()).length)) {
      Seq.empty
    } else {
      val rowString = (for (treeItem <- doxTree) yield {
        if (treeItem.isLeaf()) {
          nextRow.append(treeItem)
          if (addedLeavesList.exists(_ == treeItem)) {
            "  "
          } else {
            addedLeavesList.append(treeItem)
            treeItem.baseLabel
          }
        } else {
          nextRow.appendAll(treeItem.getChildren())
          "\\multicolumn{" + treeItem.getLeaves().length + "}{c}{" + treeItem.baseLabel + "}"
        }
      }).mkString(" & ")
      //            println(nextRow)
      Seq(rowString+"\\\\ ") ++ getRows(nextRow, addedLeavesList)
    }
  }
}
//  val queue = Queue[DoxTreeItem]()
//
//  val compareList = ListBuffer[DoxTreeItem]()
//
//  def rows(doxTree: ListBuffer[DoxTreeItem]) = {
//    // doxTree baselist
//    // newlist for subiterations
//    for ((treeItem, index) <- doxTree.zipWithIndex) yield {
//      //first add all items to comparelist which are not already appended and add them to the string
//
//      if (!compareList.exists(_ == treeItem)) {
//        compareList.append(treeItem)
//        // add multi for nodes
//        treeItem.baseLabel + " &"
//      } else {
//        if (treeItem.isLeaf()) {
//          queue.enqueue(treeItem)
//          " &"
//        } else {
//          treeItem.getChildren().map(queue.enqueue(_))
//          // doxTree.remove(index)
//        }
//      }
//    }
//
//    while (!queue.isEmpty) {
//
//      val queueItem = queue.dequeue()
//
//      if (compareList.exists(_ == queueItem)) {
//        " &"
//      }
//    }
//
//  }

//  def checkAndAppend(list: ListBuffer[DoxTreeItem], item: DoxTreeItem): String = {
//    if (item.isLeaf()) {
//      if (list.exists(_ == item)) {
//        "  &"
//      } else {
//        list.append(item)
//        item.baseLabel + " &"
//      }
//    } else {
//      val multiSize = item.getLeaves().length
//
//      "test"
//    }
//
//  }
//}
//  
    
//object SupportDoxTree {
//
//  
//  def createNode(label:String,)
//}
//  case class DataExample(workpiece: String, station: String, timestamp: String)
//
//  protected val key = new DoxTableKeyConfigSupport(MyDoxTableStringConversion(true))
//  protected val tableRef = $.tex.reference.table()
//
//  def apply() {
//    $.tex.table(tableRef, table)
//  }
//
//  protected def table() = {
//
//    val config = new DoxTableKeyConfigSupport(MyDoxTableStringConversion(false))
//
//    //    val asd = DoxTableFactory[HeaderTree](
//    //        _.caption("BLUB").indexing(false),
//    //        _.take(_.apply(_.leaves.))
//    //        )
//    val tableOverview = DoxTableFactory[DataExample](
//      _.caption("TEST").indexing(false),
//      _.take(_.apply(_.workpiece)).config(config(_.name("Workpiece").alignment(_.RIGHT).dynamic(false))),
//      _.take(_.apply(_.station)).config(config(_.name($.config.scale.name.STATION).alignment(_.RIGHT).dynamic(true))),
//      _.take(_.apply(_.timestamp)).config(config(_.name("Time").alignment(_.RIGHT).dynamic(false))))