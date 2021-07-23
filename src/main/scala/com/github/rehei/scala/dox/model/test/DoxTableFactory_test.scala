package com.github.rehei.scala.dox.model.test

import scala.collection.Seq
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableConfigBuilder
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.macros.Query

case class DoxTableFactory_test[T <: AnyRef](
  callbackConfig: DoxTableConfigBuilder.type => DoxTableConfig,
  callbackSeq:    (DoxTableFactoryKeySelection_test[T] => DoxTableFactoryKey_test)*)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()

  protected val keys = {
    callbackSeq.map(callback => callback(DoxTableFactoryKeySelection_test(query)))
  }

  protected val config = callbackConfig(DoxTableConfigBuilder)

  //  val head = keys.map(_.config)
  val head = {
    val configs = keys.map(_.config)
    val directParents = for (config <- configs) yield {
      config.parentOption.map(parent => parent).getOrElse(config)
    }
    configs
  }

  val data = ListBuffer[Seq[String]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    data.append(keys.map(key => key.getValueOf(element)))
  }

  def get() = {
    DoxTable_test(config, head, data)
  }
  protected def getRecursive() = {
    val configs = keys.map(_.config)
    //    val configHead = configs.takeWhile(!_.parentOption.isDefined)
    //    val tail = configs.dropWhile(!_.parentOption.isDefined)
    //    if (!tail.isEmpty) {
    //      tail.map(getAncestor)
    //    }
    val heads = configs.map(getAncestry)
    val rowLength = configs.length
    val heads2 = configs.filter(m => !heads.exists(_.equals(m))).map(m => getAncestryWithout(m, heads))
  }

  protected def getAncestry(current: DoxTableKeyConfig_test): DoxTableKeyConfig_test = {
    if (current.parentOption.isDefined) {
      getAncestry(current.parentOption.get)
    } else {
      current
    }
  }

  protected def getAncestryWithout(current: DoxTableKeyConfig_test, foundHeads: Seq[DoxTableKeyConfig_test]): DoxTableKeyConfig_test = {
    if (current.parentOption.isDefined) {
      if (foundHeads.exists(_.equals(current.parentOption.get))) {
        current
      } else {
        getAncestryWithout(current.parentOption.get, foundHeads)
      }
    } else {
      current
    }
  }
  protected val configs = keys.map(_.config)
  protected def rec(headssofars: ListBuffer[Option[DoxTableKeyConfig_test]] = ListBuffer.empty, counter: Int = 0): Seq[Option[DoxTableKeyConfig_test]] = {
    for (config <- configs) yield {

      if (config.parentOption.isDefined) {
        rec(headssofars.clone(), counter = counter + 1)
      } else {
        if (headssofars.exists(m => m.map(_.name == config.name).getOrElse(false))) {
          headssofars.append(None)
        } else {
          headssofars.append(Some(config))
        }
      }

    }
    headssofars.clone().toList
  }

  protected def listing() = {
    val peter = Seq[Option[DoxTableKeyConfig_test]]()
    // damit kann dummyconfig weg
  }

  protected val dummyConfig = {
    DoxTableKeyConfig_test(
      "texDummy",
      DoxTableAlignment.CENTER,
      false,
      new DoxTableStringConversion { def render(model: Any) = { "" } },
      None,
      None,
      None)
  }
}