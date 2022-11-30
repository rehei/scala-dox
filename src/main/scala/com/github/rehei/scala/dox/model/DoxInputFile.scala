package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import java.nio.file.Path

case class DoxInputFile(reference: DoxInputReference, target: DoxInputTarget)