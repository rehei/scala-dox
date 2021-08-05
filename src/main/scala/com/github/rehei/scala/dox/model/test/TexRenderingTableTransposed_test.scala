package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.tree.DoxTreeItem
import scala.collection.mutable.ListBuffer
import com.github.rehei.scala.dox.model.tree.DoxLeaf
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.dox.model.tree.DoxIndexNode
import com.github.rehei.scala.dox.model.tree.DoxPlaceholder

class TexRenderingTableTransposed_test(baseAST: TexAST, floating: Boolean, model: DoxTableNew[_], reference: DoxReferenceTable) {

  protected val markup = new TexMarkupFactory(baseAST)
  protected val treeLeaves = model.treeTable.endpointsSeq()
  import markup._

  def create() {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      $ { _ tabularx & { \\textwidth } { getColumnConfig() } } {
        \ toprule;
        //        appendTableHead()
        asd()
        //        appendTableBody()
        \ bottomrule;
      }
      \ caption & { markup.escape(model.caption) }
      \ label { reference.referenceID }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def getColumnConfig() = {
    println(adjustHead().head.length)
    //    println(adjustHead().headOption.length)
    println(adjustHead().headOption.map(_ => println("c")))
    println(adjustHead().headOption.map(_ => "c").getOrElse(""))
//    adjustHead().headOption.map(_.map(_ => "c")).getOrElse(Seq("")).mkString(" ")
    0.to(lengthasd()).map(_ => "c").mkString(" ")
    //    treeLeaves.map(node => getTexAlignment(node.config)).mkString
  }

  protected def appendTableHead() {
    for (row <- model.head) {
      \ plain { row.map(entry => columnHeader(entry)).mkString(" & ") + "\\\\" + "\n" }
    }
  }
  protected def lengthasd() = {
    adjustHead.transpose.headOption.map(_.length).getOrElse(0) +
      model.data.transpose.headOption.map(_.length).getOrElse(0)
  }

  protected def asd() = {
    println(model.data)
    println(model.data.transpose)
    println(model.head)
    println(adjustHead())
    println(adjustHead.transpose)
    //    println(model.head.trtranspose[DoxTreeItem].)
    val transi = model.data.transpose

    for ((entries, low) <- adjustHead.transpose.zip(model.data.transpose)) {
      //      println(entries.map(entry => columnHeader(entry) + " & ") + low.mkString(" & ") + "\\\\" + "\n")
      \ plain { entries.map(columnHeader).mkString(" & ") + " & "+ low.mkString(" & ") + "\\\\" + "\n" }
      \ 
      //      \ plain {  columnHeader(entry).mkString(" & ") +  low.mkString(" & ") +"\\\\" + "\n" }
    }
  }

  protected def adjustHead() = {
    for (
      row <- model.head
    ) yield {
      row.flatMap(entry => Seq(entry) ++ 0.until(entry.children.length - 1).map(_ => DoxPlaceholder()))
    }
  }
  protected def columnHeader(entry: DoxTreeItem) = {
    if (entry.endpoints().length <= 1) {
      Text2TEX.generate(entry.config.text)
    } else {
      "\\multirow{" + entry.endpoints().length + "}{*}{" + Text2TEX.generate(entry.config.text) + "}"
    }
  }
  ListBuffer(
    ListBuffer(
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("Station"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxIndexNode(DoxTableKeyConfig(TextAST(List(TextObjectDefault("#"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None)),
      DoxNode(DoxTableKeyConfig(TextAST(List(TextObjectDefault("Kapazität"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None)),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("T"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null)),
    ListBuffer(
      DoxPlaceholder(),
      DoxPlaceholder(),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("min"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("max"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxPlaceholder()))

  ListBuffer(
    ListBuffer(
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("Station"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxIndexNode(DoxTableKeyConfig(TextAST(List(TextObjectDefault("#"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None)),
      DoxNode(DoxTableKeyConfig(TextAST(List(TextObjectDefault("Kapazität"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None)),
      DoxPlaceholder(),
      DoxPlaceholder(),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("T"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null)),
    ListBuffer(
      DoxPlaceholder(),
      DoxPlaceholder(),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("min"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxLeaf(DoxTableKeyConfig(TextAST(List(TextObjectDefault("max"))), DoxTableAlignment("RIGHT"), false, TestDoxTableStringConversion(false), None), null),
      DoxPlaceholder()))

  protected def appendTableBody() {
    for (row <- model.data) yield {
      \ plain { row.map(markup.escape(_)).mkString(" & ") + "\\\\" + "\n" }
    }
  }

  protected def getTexAlignment(config: DoxTableKeyConfig) = {
    if (config.dynamic) {
      "X"
    } else {
      config.alignment match {
        case DoxTableAlignment.LEFT  => "l"
        case DoxTableAlignment.RIGHT => "r"
        case _                       => "c"
      }
    }
  }

}