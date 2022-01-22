package com.github.rehei.scala.dox.util

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Base32
import java.math.BigInteger
import org.apache.commons.codec.digest.DigestUtils
import java.security.MessageDigest
import scala.collection.JavaConversions._
import org.apache.commons.codec.digest.MessageDigestAlgorithms

object HashUtils {

  import MessageDigestAlgorithms._
  
  protected val RADIX = 32

  def hash(input: String) = {
    val md2Base16 = new DigestUtils(SHA_1).digestAsHex(input)
    val base10 = new BigInteger(md2Base16, 16)
    val base22 = base10.toString(22)

    base22.toCharArray().take(5).map(convert(_)).mkString
  }

  protected def convert(char: Char) = {
    val value = {
      if (Character.isDigit(char)) {
        char.toString().toInt
      } else {
        Character.digit(char, RADIX)
      }
    }
    Character.forDigit(value + 10, RADIX)
  }


}