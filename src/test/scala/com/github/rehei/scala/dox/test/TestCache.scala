package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyContentUniqueException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import java.nio.file.Path
import java.nio.file.Files
import scala.collection.JavaConversions._
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.util.IOUtils
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class TestCache {

  object Test extends DoxBibKeyEnum {

    object Example {
      val REINHARDT_2019 = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }
    }

    object ExampleNormalizedExt {
      val REINHARDT_2019 = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }
    }

    object ExampleNoCache {

      val PLAIN = {
        fromRAW {
          """
        @inproceedings{anything,
          title={Mathematical computations for linked data applications with openmath},
          author={Wenzel, Ken and Reinhardt, Heiner},
          booktitle={Proceedings of the 24th Workshop on OpenMath},
          pages={38--48},
          year={2012}
        }
        """
        }
      }

    }

  }

  
  @Test
  def testWarmup() {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val scanner = DoxBibKeyScanner(Test)
    val cache = DoxBibKeyCache(path).warmup(scanner.list())

    assert(cache.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).isDefined)
    assert(cache.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).isDefined)
  }

  @Test
  def testNoCache() {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)

    cache.getOrUpdate(Test.ExampleNoCache.PLAIN)

    assert(cache.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).isEmpty)
    assert(cache.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).isEmpty)
  }

  @Test
  def testCacheNormalize() {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)

    cache.getOrUpdate(Test.Example.REINHARDT_2019)

    val r1 = cache.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).get.get()
    val r2 = cache.lookupPersistentCacheValidated(Test.ExampleNormalizedExt.REINHARDT_2019).get.get()

    val doiPath = path.resolve(Test.Example.REINHARDT_2019.documentID().get.value)

    assert(r1.startsWith("@article{com-github-rehei-scala-dox-test-TestCache-Test-Example-REINHARDT_2019-UUUUUUUUU-----"))
    assertContent(r1)
    assert(r2.startsWith("@article{com-github-rehei-scala-dox-test-TestCache-Test-ExampleNormalizedExt-REINHARDT_2019-UUUUUUUUU-----"))
    assertContent(r2)

    val fileResult = IOUtils.readString(doiPath.resolve("cache.bib"))

    assert(fileResult.startsWith("@article{___"))
    assertContent(fileResult)
  }

  @Test
  def test() {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache1 = DoxBibKeyCache(path)
    val map1 = DoxBibKeyCountMap(DoxBibKeyScanner(Test).list())
    val handle1 = DoxBibKeyRendering(cache1, map1, Seq.empty)

    val cache2 = DoxBibKeyCache(path)
    val map2 = DoxBibKeyCountMap(DoxBibKeyScanner(Test).list())

    val handle2 = DoxBibKeyRendering(cache2, map2, Seq.empty)

    handle1.append(Test.Example.REINHARDT_2019)

    assertContent(cache1.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).get.get())
    assertContent(cache1.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).get.get())

    assert(cache2.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).isEmpty)
    assertContent(cache2.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).get.get())
    assert(cache2.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).isEmpty)

    assert(cache2.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).isEmpty)
    assertContent(cache2.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).get.get())

    assertContent(cache2.getOrUpdate(Test.Example.REINHARDT_2019).get())
    assertContent(cache2.lookupMemoryCacheValidated(Test.Example.REINHARDT_2019).get.get())
    assertContent(cache2.lookupPersistentCacheValidated(Test.Example.REINHARDT_2019).get.get())

  }

  protected def assertContent(content: String) {
    assert(content.contains("doi = {10.1016/j.procir.2019.03.022}"))
    assert(content.contains("author = {Heiner Reinhardt and Marek Weber and Matthias Putz}"))
    assert(content.contains("title = {A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing}"))
  }

}