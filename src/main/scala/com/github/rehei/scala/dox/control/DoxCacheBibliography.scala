package com.github.rehei.scala.dox.control

import java.nio.charset.StandardCharsets
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import java.nio.file.Files
import scala.collection.JavaConversions._
import java.nio.file.StandardOpenOption
import com.github.rehei.scala.dox.model.DoxDOI
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyLookupResult
import com.github.rehei.scala.dox.model.bibliography.DoxBibtexParse

case class DoxCacheBibliography(target: Path, warmup: Seq[DoxBibKey]) {

  if (!Files.exists(target)) {
    Files.createDirectories(target)
  }

  protected val memoryCache = scala.collection.mutable.Map[DoxDOI, DoxBibKeyLookupResult]()

  for (key <- warmup) {
    getOrUpdate(key)
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