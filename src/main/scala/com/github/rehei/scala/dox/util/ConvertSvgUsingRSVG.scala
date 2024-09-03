package com.github.rehei.scala.dox.util

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import scala.collection.Seq
import scala.sys.process.Process

class ConvertSvgUsingRSVG(protected val mode: SvgMode, protected val baseDirectory: Path) {

  def transform() {
    Process(executable, baseDirectory.toFile()).!
  }

  protected def executable = {
    val commandFile = baseDirectory.resolve("shellscript.sh")
    IOUtils.writeString(commandFile, command)
    commandFile.toFile().setExecutable(true, true)
    commandFile.toAbsolutePath().toString()
  }

  protected def command = {
    val processors = Math.max((Runtime.getRuntime().availableProcessors() * 0.7).toInt, 1)
    "for f in *.svg; do echo rsvg-convert " + mode.commandRSVG("f") + " ${f}; done | xargs -P" + processors + " -i sh -c {}"
  }

}