package com.github.rehei.scala.dox.control.tex

class TexBuilderCommand(ast: TexAST) {

  protected abstract class TexMarkupObject(inline: Boolean) extends TexCommand(inline, ast, TexSeq(Seq.empty)) {
    def name = {
      this.getClass.getName.replace("$$", "*").stripSuffix("$").split("\\$").last
    }
  }

  object FloatBarrier extends TexMarkupObject(false)
  object footnotesize extends TexMarkupObject(false)
  object centering extends TexMarkupObject(false)
  object toprule extends TexMarkupObject(false)
  object midrule extends TexMarkupObject(false)
  object bottomrule extends TexMarkupObject(false)
  object cmidrule extends TexMarkupObject(false)
  object multicolumn extends TexMarkupObject(false)

  object fill extends TexMarkupObject(false)
  object includegraphics extends TexMarkupObject(false)
  object caption extends TexMarkupObject(false)
  object chapter extends TexMarkupObject(false)
  object chapter$ extends TexMarkupObject(false)
  object section extends TexMarkupObject(false)
  object section$ extends TexMarkupObject(false)
  object subsection extends TexMarkupObject(false)
  object subsection$ extends TexMarkupObject(false)
  object subsubsection extends TexMarkupObject(false)
  object subsubsection$ extends TexMarkupObject(false)
  object vspace extends TexMarkupObject(false)
  object vspace$ extends TexMarkupObject(false)
  object hspace extends TexMarkupObject(false)

  object clearpage extends TexMarkupObject(false)
  object newpage extends TexMarkupObject(false)
  object bigskip extends TexMarkupObject(false)
  object label extends TexMarkupObject(true)

  object usepackage extends TexMarkupObject(false)

  object begin extends TexMarkupObject(false)
  object end extends TexMarkupObject(false)

  object textit extends TexMarkupObject(true)
  object textbf extends TexMarkupObject(true)

  object textcolor extends TexMarkupObject(true)
  object item extends TexMarkupObject(true)
  object ref extends TexMarkupObject(true)

  object rule extends TexMarkupObject(true)

  object cite extends TexMarkupObject(true)
  object citep extends TexMarkupObject(true)
  object citet extends TexMarkupObject(true)

  object onecolumn extends TexMarkupObject(false)
  object twocolumn extends TexMarkupObject(false)

  object input extends TexMarkupObject(false)
  object includesvgImage extends TexMarkupObject(false)
  object tag extends TexMarkupObject(true)

  object plain {
    def apply(_name: String) = {
      TexPlain(ast, _name)
    }
  }

}