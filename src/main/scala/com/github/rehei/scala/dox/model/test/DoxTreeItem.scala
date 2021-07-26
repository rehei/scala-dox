package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

abstract class DoxTreeItem(label: String /*, textSettings: DoxTableKeyConfig_test*/ ) {
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

  override def toString() = {
    this match {
      case leaf @ DoxLeaf(_, _) => "Leaf:" + leaf.label + ", " + leaf.value
      case node @ DoxNode(_, _) => "Node:" + node.label + "," + node.children.map(_.toString())
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }
}

case class DoxLeaf(label: String, /*textSettings: DoxTableKeyConfig_test,*/ value: String) extends DoxTreeItem(label /*, textSettings*/ )
case class DoxNode(label: String, /*textSettings: DoxTableKeyConfig_test, */ children: Seq[DoxTreeItem]) extends DoxTreeItem(label /*, textSettings*/ )

object MakeDoxTree {

  def treeHead(label: String) = new MakeDoxTree {
    //    val asd = withNode("peter").andSubItems(Seq(DoxLeaf("bla", "test"), DoxNode("asd", Seq.empty), DoxLeaf("lis", "glsd")))

  }
}

abstract class MakeDoxTree() {

  val doxTree = ListBuffer[DoxTreeItem]()
  def leaf(label: String, value: String) = {
    doxTree.append(DoxLeaf(label, value))
    this
  }
//  def node(label: String, children: Seq[DoxTreeItem]) = {
//    doxTree.append(DoxNode(label, children))
//    this
//  }
  def node(label: String) = new MakeDoxTree{

    def get() = {
      DoxNode(label,this.doxTree)
    }
  }
}

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