package com.github.rehei.scala.dox.model.bibliography

import scala.collection.mutable.HashMap
import com.github.rehei.scala.dox.model.ex.DoxBibKeyCountStrictException

object DoxBibKeyCountMap {
  def apply(sequence: Seq[DoxBibKey]): DoxBibKeyCountMap = {
    DoxBibKeyCountMap(sequence, true)
  }
}

case class DoxBibKeyCountMap private (protected val sequence: Seq[DoxBibKey], protected val strict: Boolean) {

  protected val map = HashMap[DoxBibKey, Int]()

  for (key <- sequence) {
    map.put(key, 0)
  }

  def increase(key: DoxBibKey) = {
    val value = map.get(key)

    if (strict && value.isEmpty) {
      throw new DoxBibKeyCountStrictException("This should already be initialized by scanner.")
    }

    val count = value.getOrElse(0)
    map.put(key, count + 1)
  }

  def strict(enable: Boolean) = {
    this.copy(strict = enable)
  }

  def listZero() = {
    this.listAll().filter(_.count == 0)
  }

  def listMoreThanZero() = {
    this.listAll().filter(_.count > 0)
  }

  def listAll() = {
    map.map(entry => DoxBibKeyCount(entry._1, entry._2)).toSeq
  }

}