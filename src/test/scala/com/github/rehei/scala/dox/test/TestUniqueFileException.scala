package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.file.DoxFileEnum
import java.nio.file.Paths
import org.junit.Test
import scala.collection.mutable.HashMap
import com.github.rehei.scala.dox.model.table.DoxTableFile

class TestUniqueFileException extends DoxFileEnum(None) {
  class UniqueFileNameException(errorText: String) extends Exception(errorText) {
    def this(errorText: String, reason: Throwable) {
      this(errorText)
      initCause(reason)
    }
    def this(reason: Throwable) {
      this(Option(reason).map(_.toString()).orNull, reason)
    }
    def this() {
      this(null: String)
    }
  }

  val inmemory = Paths.get("/mnt/inmemory/")
  val peter = unique
  val tmp = inmemory.resolve("./datax/datax-" + "abc")
  tmp.toFile().mkdirs()

  protected val TARGET = tmp.normalize()
  println(TARGET)
  protected val TARGET_DUMMY = TARGET.resolve("dmy")
  protected val usage = HashMap[String, Boolean]()
  protected val nextID = DoxReferenceFactory("")
  protected val prefix = "blub"
  protected var count = 0

  @Test(expected = classOf[UniqueFileNameException])
  def test() {
    // Wegen generateFileName wird der Durchlauf partiell und vollständig hunderte Male aufgerufen, warum?
    //    val samefilename1 = generateFileName
    //    println(count)
    //    count += 1
    val samefilename1 = "peter.tex"
    addAndCheckFileName(samefilename1)
    //    println("aaa")
    val samefilename2 = "peter.tex" // generateFileName
    addAndCheckFileName(samefilename2)
    //    println("ddd")
  }

  protected def addAndCheckFileName(filename: String) = {
    //    println(filename)
    //    println("asd")
    if (!usage.get(filename).isEmpty) {
      throw new UniqueFileNameException(usage.size.toString())
    }
    usage.put(filename, true)
  }
  protected def generateFileName() = {
    val tableFile = DoxTableFile("some latex", peter.get())
    tableFile.label.map(_.name + ".tex").getOrElse(generateName)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.tex"

  }
}