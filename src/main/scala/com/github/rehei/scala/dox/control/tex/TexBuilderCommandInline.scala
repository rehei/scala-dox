package com.github.rehei.scala.dox.control.tex

class TexBuilderCommandInline(ast: TexAST) {

  protected class TexMarkupObject extends TexCommandInline(true, TexSeq(Seq.empty)) {
    def name = {
      this.getClass.getName.stripSuffix("$").split("\\$").last
    }
  }

  object cmidrule extends TexMarkupObject
  object multicolumn extends TexMarkupObject
  object textwidth extends TexMarkupObject

}