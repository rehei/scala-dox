package com.github.rehei.scala.dox.model.bibliography

import org.jbibtex.BibTeXDatabase
import com.github.rehei.scala.dox.util.NormalizeUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException
import org.jbibtex.Key
import scala.collection.JavaConversions._

case class DoxBibtexParseSingleEntry(protected val bibKeyName: String, protected val database: BibTeXDatabase) {

  assert(database.getEntries.values().toSeq.size == 1)
  protected val entry = database.getEntries.values().toSeq.head

  def asDatabase = {
    database
  }
  
  def expectNormalized(key: Key, expected: String) = {
    val actual = entry.getField(key).toUserString()
    if (NormalizeUtils.normalizeTex(actual) != NormalizeUtils.normalizeString(expected)) {
      throw new DoxBibKeyIntegrityException(getExceptionMessage(key, expected, actual))
    }
  }

  def expectAnyWordNormalized(key: Key, expected: String) = {
    val actual = entry.getField(key).toUserString()

    val actualWordSeq = actual.split("\\s").map(NormalizeUtils.normalizeTex(_))
    val exepectedWordSeq = expected.split("\\s").map(NormalizeUtils.normalizeString(_))

    val matchingResult = actualWordSeq.intersect(exepectedWordSeq)
    if (matchingResult.isEmpty) {
      throw new DoxBibKeyIntegrityException(getExceptionMessage(key, expected, actual))
    }
  }

  protected def getExceptionMessage(key: Key, expected: String, actual: String) = {
    s"Checking integrity for ${bibKeyName} on ${key.getValue} failed, as ${expected} was expected, but actually ${actual} was given."
  }
  
 

}