package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory

object ExampleTex {

  def main(args: Array[String]): Unit = {

    val ast = TexAST()
    val markup = new TexMarkupFactory(ast)
    import markup._

    \ FloatBarrier

    \ includegraphics & { ###("scale=0.5") } { "bla" } { "bli" } { "foo" } { "bli" }
    \ includegraphics & { ###("scale=0.5") } { "bla" } { "bli" } { "foo" }
    \ includegraphics & { ###("scale=0.5") } { "bla" } { "bli" }
    \ includegraphics & { ###("scale=0.5") } { "bla" }

    \ includegraphics & { ###("bla") }

    \ includegraphics & { ###("bla") } { "li" }
    \ includegraphics & { "sd" } { "fl" }

    \ includegraphics & { "sd" }

    \ includegraphics;
    $ { _ itemize & { ###("foo") } { "lv" } } {
      \ includegraphics
    }

    $ { _ itemize & { ###("foo") } { "bla" } { "bli" } } {
      \ includegraphics
    }

    $ { _ itemize & { "foo" } { "bla" } } {
      \ includegraphics
    }

    $ { _ itemize & { \\textwidth } { "bla" } } {
      \ includegraphics
    }

    $ { _ itemize & { ###("foo") } } {
      \ includegraphics;
      \ plain { "FOOO" }
    }

    println(ast.build())

  }

}