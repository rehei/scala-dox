package com.github.rehei.scala.dox.control.tex

class TexBuilderCommandInline(ast: TexAST) {

  protected class TexMarkupObject extends TexCommandInline(true, TexSeq(Seq.empty)) {
    def name = {
      this.getClass.getName.stripSuffix("$").split("\\$").last
    }
  }

  object cmidrule extends TexMarkupObject
  object multicolumn extends TexMarkupObject
  object rotatebox extends TexMarkupObject
  object textwidth extends TexMarkupObject
  object hsize extends TexMarkupObject
  object hspace extends TexMarkupObject
  object dimexpr extends TexMarkupObject

  object tabcolsep extends TexMarkupObject
  object p extends TexMarkupObject

  object plain extends TexMarkupObject {

    protected val self = this

    override def name() = {
      throw new UnsupportedOperationException()
    }
    override def generate() = {
      extension() + args.generate() + extension()
    }
    override protected def create(in: TexSeq) = {
      new TexCommandInline(inline, args.append(in)) {
        override def name() = self.name()
        override def generate() = {
          extension() + args.generate() + extension()
        }
      }
    }
  }

}