package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

case class DoxFileTable(content: String, label: Option[DoxReferencePersistentTable]) 