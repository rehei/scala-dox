package com.github.rehei.scala.dox.util

import java.nio.file.Files
import java.nio.file.Paths
import org.apache.commons.io.FileUtils

object FileFactory {

  protected val enableInMemory = true

  protected val globalNext = NextID("repository-" + System.currentTimeMillis() + "-")

  def create() = {
    val inmemory = Paths.get("/mnt/inmemory/")

    if (enableInMemory && inmemory.toFile().canWrite()) {

      /*
       * You may add the following line to /etc/fstab
       * none 	/mnt/inmemory  	tmpfs   size=1024M 0 0
       */

      val tmp = inmemory.resolve("./datax/datax-" + globalNext.nextID())
      tmp.toFile().mkdirs()
      tmp
    } else {
      Files.createTempDirectory("datax-" + globalNext.nextID())
    }
  }
  
}

class FileFactory {
  
  protected val next = NextID("database")

  val base = {
    FileFactory.create()
  }

  def clean() = {
    FileUtils.deleteDirectory(base.toFile())
  }

  protected val cachePersistentAny = {
    val homeDir = Paths.get(System.getProperty("user.home"))
    homeDir.resolve("./datax-persistent/")
  }

  val cachePersistentBibtex = {
    cachePersistentAny.resolve("./bibtex-cache/")
  }

}