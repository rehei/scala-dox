package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

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
    """\begin{table}
        \centering;
        \begin{tabularx}{\textwidth } {""" + doxTree.leaves().map(_ => "c").mkString(" ") + """} {
        \toprule
        """ + {
      //          for(treeItem <- doxTree.doxTreeHeadSeq) yield{
      //
      //          }
      doxTree.doxTreeHeadSeq.map(_.baseLabel + " &").mkString(" ")
    } +
      """
        \midrule
        """ +
      //appendTableBody()
      """
          \bottomrule
        }
      }
      """
  }
  def checkAndAppend(list: ListBuffer[DoxTreeItem], item: DoxTreeItem):String = {
    if (item.isLeaf()) {
      if (list.exists(_ == item)) {
        "  &"
      } else {
        list.append(item)
        item.baseLabel + " &"
      }
    } else {
      val multiSize = item.getLeaves().length

      "test"
    }

  }
}
  
    
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