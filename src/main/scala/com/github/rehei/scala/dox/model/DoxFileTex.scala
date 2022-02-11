package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTex
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxFileTex(content: String, label: Option[DoxReferenceBase]) 