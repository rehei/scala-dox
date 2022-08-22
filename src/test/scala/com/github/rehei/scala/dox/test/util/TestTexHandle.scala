package com.github.rehei.scala.dox.test.util

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.rehei.scala.dox.util.TexUtils
import com.github.rehei.scala.dox.util.FileFactory

class TestTexHandle {

  protected val FILE_FACTORY = new FileFactory

  protected val SOURCE = Paths.get("./src/main/resources")
  protected val TARGET = FILE_FACTORY.base.resolve("./dashboard-rendering-latex")
  protected val TARGET_INCLUDE_MAIN = TARGET.resolve("./generated_main.tex")

  protected val tex = new TexUtils(TARGET, "document", false)
  protected val tableHandle = DoxHandleTable(TARGET, TARGET.resolve("tex-table"))
  protected val texMainAST = TexAST()

  val texMain = new TexRendering(texMainAST, false, null, null, tableHandle, null, null)

  def generate() = {
    write()
    tex.generatePDF()
  }

  protected def write() {
    FileUtils.copyDirectory(SOURCE.toFile(), TARGET.toFile())
    val writer = Files.newBufferedWriter(TARGET_INCLUDE_MAIN, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
    texMainAST.write(writer)
    writer.close()

  }

}