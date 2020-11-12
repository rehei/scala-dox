package com.github.rehei.scala.dox.model.bibliography

import java.nio.file.Path
import java.nio.file.Files
import scala.collection.JavaConversions._
import java.nio.file.StandardOpenOption
import com.github.rehei.scala.dox.model.DoxDOI
import scala.collection.Seq

object DoxBibKeyCache {

  def apply(target: Path): DoxBibKeyCache = {
    DoxBibKeyCache(target, Seq.empty)
  }
  
  protected def apply(target: Path, warmup: Seq[DoxBibKey]) = {
    new DoxBibKeyCache(target, warmup)
  }

}

case class DoxBibKeyCache protected(target: Path, warmup: Seq[DoxBibKey]) {

  if (!Files.exists(target)) {
    Files.createDirectories(target)
  }

  protected val memoryCache = scala.collection.mutable.Map[DoxDOI, DoxBibKeyLookupResult]()

  for (key <- warmup) {
    getOrUpdate(key)
  }

  def warmup(sequence: Seq[DoxBibKey]) = {
    this.copy(warmup = sequence)
  }

  def getOrUpdate(key: DoxBibKey) = {
    lookupMemoryCache(key).getOrElse {
      lookupPersistentCache(key).getOrElse {
        val content = lookupDelegate(key)
        updateCache(key, content)
        content
      }
    }
  }

  protected def lookupMemoryCache(key: DoxBibKey) = {
    key.documentID.flatMap(doi => memoryCache.get(doi))
  }

  protected def lookupPersistentCache(key: DoxBibKey) = {
    for (doi <- key.documentID; cacheFile = path(doi) if Files.exists(cacheFile)) yield {
      val tmp = new String(Files.readAllBytes(cacheFile))
      val result = DoxBibKeyLookupResult(key.name, DoxBibtexParse().parse(tmp))
      memoryCache.put(doi, result)
      result
    }
  }

  protected def lookupDelegate(key: DoxBibKey) = {
    key.lookup.resolve()
  }

  protected def updateCache(key: DoxBibKey, result: DoxBibKeyLookupResult) {
    for (doi <- key.documentID) {
      memoryCache.put(doi, result)
      Files.write(path(doi), result.normalize().getBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
    }
  }

  protected def path(doi: DoxDOI) = {
    val path = target.resolve(doi.value)
    Files.createDirectories(path)
    path.resolve("cache.bib")
  }

}