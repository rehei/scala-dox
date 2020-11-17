package com.github.rehei.scala.dox.model.bibliography

import java.nio.file.Path
import java.nio.file.Files
import scala.collection.JavaConversions._
import java.nio.file.StandardOpenOption
import com.github.rehei.scala.dox.model.DoxValueDOI
import scala.collection.Seq
import com.github.rehei.scala.dox.util.IOUtils

object DoxBibKeyCache {

  def apply(target: Path): DoxBibKeyCache = {
    DoxBibKeyCache(target, Seq.empty)
  }

  protected def apply(target: Path, warmup: Seq[DoxBibKey]) = {
    new DoxBibKeyCache(target, warmup)
  }

}

case class DoxBibKeyCache protected (target: Path, warmup: Seq[DoxBibKey]) {

  case class LookupSupport(result: DoxBibKeyLookupResult, updateMemory: Boolean, updatePersistent: Boolean)

  protected val memoryCache = scala.collection.mutable.Map[DoxValueDOI, DoxBibKeyLookupResult]()

  for (key <- warmup) {
    getOrUpdate(key)
  }

  def warmup(sequence: Seq[DoxBibKey]) = {
    this.copy(warmup = sequence)
  }

  def getOrUpdate(key: DoxBibKey) = {

    val lookup = {
      lookupMemoryCacheValidated(key).map(LookupSupport(_, false, false)).getOrElse {
        lookupPersistentCacheValidated(key).map(LookupSupport(_, true, false)).getOrElse {
          LookupSupport(lookupDelegateValidated(key), true, true)
        }
      }
    }

    for (doi <- key.documentID) {
      if (lookup.updateMemory) {
        memoryCache.put(doi, lookup.result)
      }
      if (lookup.updatePersistent) {
        IOUtils.writeString(path(doi), lookup.result.normalize())
      }
    }

    lookup.result
  }

  def lookupMemoryCacheValidated(key: DoxBibKey) = {
    for (doi <- key.documentID; result <- memoryCache.get(doi)) yield {
      key.lookup.validate(result)
      result
    }
  }

  def lookupPersistentCacheValidated(key: DoxBibKey) = {
    for (doi <- key.documentID; cacheFile = path(doi) if Files.exists(cacheFile)) yield {
      val tmp = IOUtils.readString(cacheFile)
      val database = DoxBibtexParse().parse(tmp)
      val result = DoxBibKeyLookupResult(key.name, database)
      key.lookup.validate(result)
      result
    }
  }

  protected def lookupDelegateValidated(key: DoxBibKey) = {
    key.lookup.resolveValidated()
  }

  protected def path(doi: DoxValueDOI) = {
    target.resolve(doi.value).resolve("cache.bib")
  }

}