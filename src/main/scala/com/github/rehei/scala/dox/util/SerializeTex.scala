package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxFileTex

class SerializeTex(baseDirectory: Path) extends SerializeBase(baseDirectory, "tex") {

  def generate(tex: DoxFileTex) = {
    super.write(tex.content, tex.label, ".tex")
  }
  
}