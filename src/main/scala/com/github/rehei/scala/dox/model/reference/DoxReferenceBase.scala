package com.github.rehei.scala.dox.model.reference

import java.math.BigInteger
import org.apache.commons.codec.digest.MessageDigestAlgorithms
import org.apache.commons.codec.digest.DigestUtils
import com.github.rehei.scala.dox.util.HashUtils

abstract class DoxReferenceBase {

  val name: String

}