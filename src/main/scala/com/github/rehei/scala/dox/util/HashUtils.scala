package com.github.rehei.scala.dox.util

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Base32
import java.math.BigInteger
import org.apache.commons.codec.digest.DigestUtils
import java.security.MessageDigest


object HashUtils {
    def hash(input: String) = {
    val md2Base16 = new DigestUtils(org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1).digestAsHex(input)
    val base10 = new BigInteger(md2Base16, 16)
    val base64 = new String(Base64.encodeInteger(base10))
    base64.substring(0, 5).replace('/', '-')
  }
}