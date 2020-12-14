package com.github.rehei.scala.dox.control.tex

class TexBuilderCommand(ast: TexAST) {

  protected class TexMarkupObject extends TexCommand(ast, TexSeq(Seq.empty)) {
    def name = {
      this.getClass.getName.replace("$$", "*").stripSuffix("$").split("\\$").last
    }
  }

  object FloatBarrier extends TexMarkupObject
  object centering extends TexMarkupObject
  object toprule extends TexMarkupObject
  object midrule extends TexMarkupObject
  object bottomrule extends TexMarkupObject
  object includegraphics extends TexMarkupObject
  object caption extends TexMarkupObject
  object chapter extends TexMarkupObject
  object chapter$ extends TexMarkupObject
  object section extends TexMarkupObject
  object section$ extends TexMarkupObject
  object subsection extends TexMarkupObject
  object subsection$ extends TexMarkupObject
  object subsubsection extends TexMarkupObject
  object subsubsection$ extends TexMarkupObject

  object textit extends TexMarkupObject

  object item extends TexMarkupObject
  object clearpage extends TexMarkupObject
  object newpage extends TexMarkupObject
  object label extends TexMarkupObject
  object ref extends TexMarkupObject
  object usepackage extends TexMarkupObject

  object cite extends TexMarkupObject

  object citep extends TexMarkupObject
  object citet extends TexMarkupObject

  object begin extends TexMarkupObject
  object end extends TexMarkupObject

  object plain {
    def apply(_name: String) = {
      TexPlain(ast, _name)
    }
  }

}