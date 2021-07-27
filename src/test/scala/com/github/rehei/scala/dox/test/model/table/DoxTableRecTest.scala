package com.github.rehei.scala.dox.test.model.table

import scala.collection.mutable.ListBuffer

import org.junit.Test

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.test.DoxTableKeyConfig_test
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion

import com.github.rehei.scala.dox.model.test.DoxTreeItem
import com.github.rehei.scala.dox.model.test.MakeDoxTree

class DoxTableRecTest {
  case class DoxConfigCounter(config: DoxTableKeyConfig_test, var counter: Int)
  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)
  
  protected val configs = getDummys()

  @Test
  def test() {
    //    println(petersTree.doxTree.toSeq.length)
    //ataExample(workpiece: String, station: String, timestamp: String)
    val petersTree = MakeDoxTree.treeHead("StationSetup")
    //    petersTree.addNode("asd").addC
    val blub = petersTree
      .addLeaf("Station", "station")
      .addNode("Kapazität")
      .addChildren(
        _
          .addLeaf("min", "somemin")
          .addLeaf("max", "somemax"))
      .addLeaf("T", "time")

    println(blub.doxTreeHeadSeq.map(_.baseLabel))
    println(blub.doxTreeHeadSeq)

    //    val bla = petersTree
    //      .addLeaf("Station", "station")
    //      .addNode("Kapazität")
    //        .addLeaf("min", "somevalue")
    //      .addLeaf("max", "somemaxvalue")
    //      .addLeaf("T", "time")
    //      .doxTreeHeadSeq
    //      petersTree.addNode("bla").addChildItems().add

    //      println(bla)
    //  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)
    //
    //  def generate() = {

    //    val config = new DoxTableKeyConfigSupport(MyDoxTableStringConversion(false))
    //
    //    val tableOverview = DoxTableFactory[StationSetup](
    //      _.caption("TEST").indexing(false),
    //      _.take(_.apply(_.station)).config(config(_.name($.config.scale.name.STATION).alignment(_.RIGHT).dynamic(true))),
    //      _.take(_.apply(_.capacityMin)).config(config(_.name("K (min)").alignment(_.RIGHT).dynamic(false))),
    //      _.take(_.apply(_.capacityMax)).config(config(_.name("K (max)").alignment(_.RIGHT).dynamic(false))),
    //      _.take(_.apply(_.time)).config(config(_.name("T").alignment(_.RIGHT).dynamic(false))))    //    getAncestorWithMulti

    //    getResultRows
    //    println(getDemConfigs.map(_.map(_.map(_.name).getOrElse("Emptyspot"))))
  }

//    override def create() {
//    
//    $ { _ table & { ###("H!") } } {
//      \ centering;
//      $ { _ tabularx & { \\hsize } { getTableConfig() } } {
//        \ hline;
//
//        \ hline;
//        \ hline;
//      }
//      \ caption & { markup.escape(model.caption()) }
//      \ label { reference.referenceID }
//    }
//  }
    
  protected def getDummys() = {
    (for (i <- 1 to 10) yield {
      dummyConfig(i, Some(parentDummyConfig(i)))

    }) ++
      (for (i <- 11 to 15) yield {
        dummyConfig(i, None)
      }) ++
      (for (i <- 16 to 20) yield {
        dummyConfig(i, Some(parentDummyConfig(i - 10)))

      }) ++
      (for (i <- 26 to 30) yield {
        dummyConfig(i, Some(parentDummyConfig(i - 22)))

      })

  }
  // am anfang alle top level nehmen, hochzählen für höchste treffer
  // nächster durchlauf , köpfe rausnehmen
  //  protected def getResultRows() = {
  //    val foundHeads = ListBuffer[DoxTableKeyConfig_test]()
  //    val rows = ListBuffer[Seq[Option[DoxTableKeyConfig_test]]]()
  //
  //    var row = Seq[Option[DoxTableKeyConfig_test]]()
  //    do {
  //      row = {
  //        for (currentConfig <- configs) yield {
  //          val config = getAncestor(currentConfig, foundHeads)
  //          println(config.name)
  //          if (!foundHeads.exists(_.name == config.name)) {
  //            //            println(config.name)
  //            foundHeads.append(config)
  //            Some(config)
  //          } else {
  //            None
  //          }
  //        }
  //      }
  //      //      println(row)
  //      if (row.exists(_.isDefined)) {
  //        rows.append(row)
  //      }
  //    } while (row.exists(_.isDefined))
  //    //      println(rows)
  //    rows
  //  }

  //  protected def getRow(foundHeads: ListBuffer[DoxTableKeyConfig_test]) = {
  //
  //  }

  protected def getAncestorWithMulti() = {
    val listSeq = ListBuffer[Seq[DoxConfigCounter]]()
    val listing = ListBuffer[DoxConfigCounter]()
    val listIgnore = ListBuffer[DoxConfigCounter]()
    for (config <- configs) yield {
      val ancestor = getAnces(config)
      if (listing.exists(m => m.config.name == ancestor.name)) {
        listing.find(m => m.config.name == ancestor.name).map(_.counter += 1)
      } else {
        listing.append(DoxConfigCounter(ancestor, 1))
      }
    }
    println(listing.map(_.config.name))
    println(listing.map(_.counter))
    listSeq.append(listing)
    listIgnore.appendAll(listing)
    listing.clear()
    listing.map(_.config.name)

  }

  protected def getAnces(current: DoxTableKeyConfig_test): DoxTableKeyConfig_test = {
    if (current.parentOption.isDefined) {
      getAnces(current.parentOption.get)
    } else {
      current
    }
  }

  //  protected def getAncestor(current: DoxTableKeyConfig_test, foundHeads: ListBuffer[DoxTableKeyConfig_test], counter: Int = 0): DoxTableKeyConfig_test = {
  //    if (current.parentOption.isDefined) {
  //      if (foundHeads.exists(_.name == current.parentOption.get.name)) {
  //
  //        current
  //      } else {
  //        getAncestor(current.parentOption.get, foundHeads, counter + 1)
  //      }
  //
  //    } else {
  //      current
  //    }
  //  }
  //
  //  protected def rec(addedNodes: ListBuffer[Option[DoxTableKeyConfig_test]] = ListBuffer.empty, counter: Int = 1): Seq[Option[DoxTableKeyConfig_test]] = {
  //
  //    val all = ListBuffer[Seq[Option[DoxTableKeyConfig_test]]]()
  //
  //    for (config <- configs) yield {
  //
  //      if (config.parentOption.isDefined) {
  //        all.append(rec(addedNodes.clone(), counter = counter + 1))
  //      } else {
  //        if (addedNodes.exists(m => m.map(_.name == config.name).getOrElse(false))) {
  //          addedNodes.append(None)
  //        } else {
  //          val multiColumn = {
  //            if (counter > 1) {
  //              Some(counter)
  //            } else {
  //              None
  //            }
  //          }
  //          addedNodes.append(Some(config.copy(multiColumn = multiColumn)))
  //        }
  //      }
  //
  //    }
  //    addedNodes.clone().toList
  //  }

  protected def dummyConfig(index: Int, parent: Option[DoxTableKeyConfig_test]) = {
    DoxTableKeyConfig_test(
      "texDummy" + index,
      DoxTableAlignment.CENTER,
      false,
      new DoxTableStringConversion { def render(model: Any) = { "" } },
      None,
      None,
      parent)
  }

  protected def parentDummyConfig(index: Int) = {
    DoxTableKeyConfig_test(
      "textParent" + index,
      DoxTableAlignment.CENTER,
      false,
      new DoxTableStringConversion { def render(model: Any) = { "" } },
      None,
      None,
      None)
  }
}