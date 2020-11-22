package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.util.IOUtils
import java.io.StringWriter

class TestSpecialCharacters {

  object Example extends DoxBibKeyEnum {

    val MÜLLER_2018 = {
      fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
        .year(2018).by("Müller et al.").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
    }

    val VEJAR_2011 = {
      fromDOI("https://doi.org/10.1007/s10845-011-0504-x")
        .year(2011).by("Andrés Véjar et al.").title("Generation of an adaptive simulation driven by product trajectories")
    }

    val STRAßBURGER_2008 = {
      fromDOI("http://dx.doi.org/10.1109/WSC.2008.4736140")
        .year(2008).by("Strassburger et al.").title("Future trends in distributed simulation and distributed virtual environments: Results of a peer study")
    }

  }
  @Test
  def test() {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)
    val map = DoxBibKeyCountMap(Seq.empty).strict(false)
    val handle = DoxBibKeyRendering(cache, map)

    val output = path.resolve("output.bib")

    handle.append(Example.MÜLLER_2018)
    handle.append(Example.VEJAR_2011)
    handle.append(Example.STRAßBURGER_2008)

    val writer = new StringWriter()
    
    handle.write(writer)

    val content = writer.toString()

    assert(content.contains("com-github-rehei-scala-dox-test-TestSpecialCharacters-Example-MUELLER_2018-UUUUUUU"))
    assert(content.contains("com-github-rehei-scala-dox-test-TestSpecialCharacters-Example-VEJAR_2011-UUUUU"))
    assert(content.contains("com-github-rehei-scala-dox-test-TestSpecialCharacters-Example-STRAssBURGER_2008-UUUULLUUUUUU"))
    
  }

}