package com.github.rehei.scala.dox.model.bibliography

import java.nio.file.Path

import scala.collection.mutable.HashSet
import scala.collection.mutable.Map

import com.github.rehei.scala.dox.model.ex.DoxBibKeyContentUniqueException
import com.github.rehei.scala.dox.util.IOUtils
import java.io.Writer
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameAsciiException
import com.github.rehei.scala.dox.model.validation.IDoxBibKeyValidation
import com.github.rehei.scala.dox.model.validation.IDoxBibKeyValidation
import com.github.rehei.scala.dox.model.validation.IDoxBibKeyValidation
import com.github.rehei.scala.dox.model.validation.DoxBibKeyValidationNameAscii
import com.github.rehei.scala.dox.model.validation.DoxBibKeyValidationNameRequired

object DoxBibKeyRendering {
  def apply(cache: DoxBibKeyCache, map: DoxBibKeyCountMap): DoxBibKeyRendering = {
    DoxBibKeyRendering(cache, map,
      Seq(new DoxBibKeyValidationNameAscii(), new DoxBibKeyValidationNameRequired()))
  }
}

case class DoxBibKeyRendering private (cache: DoxBibKeyCache, map: DoxBibKeyCountMap, protected val validationSeq: Seq[IDoxBibKeyValidation]) {

  protected val keys = HashSet[DoxBibKey]()
  protected val inverseKeyLookup = Map[String, String]()

  def validateUsing(validation: IDoxBibKeyValidation) = {
    this.copy(validationSeq = validationSeq :+ validation)
  }

  def append(key: DoxBibKey) {

    for (validation <- validationSeq) {
      validation.validate(key)
    }

    map.increase(key)

    val result = cache.getOrUpdate(key)
    val content = result.normalize()

    inverseKeyLookup.get(content).map {
      referenceKey =>
        {
          if (key.name != referenceKey) {
            throw new DoxBibKeyContentUniqueException("LookupKeys should be referenced uniquely")
          }
        }
    } getOrElse {
      inverseKeyLookup.put(content, key.name)
    }

    keys.add(key)

  }

  def write(writer: Writer) = {
    for (referenceKey <- keys) {
      writer.write(cache.getOrUpdate(referenceKey).get())
    }
  }

}