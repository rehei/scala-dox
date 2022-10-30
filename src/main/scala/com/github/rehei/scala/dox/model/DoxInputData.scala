package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.util.HashUtils

case class DoxInputData(reference: DoxInputReference, val content: String)