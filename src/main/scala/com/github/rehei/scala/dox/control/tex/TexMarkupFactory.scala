package com.github.rehei.scala.dox.control.tex

import scala.collection.mutable.ListBuffer
import com.github.rehei.scala.macros.Query
import scala.xml.Elem
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets
import scala.collection.mutable.Stack

class TexMarkupFactory(protected val ast: TexAST) {

  protected class TexCommandExpressionBuilder[T](ast: TexAST, create: (TexAST, String, TexSeq) => T) {
    protected val $ = new Query[this.type]

    implicit def query2command(base: Query[_]) = {
      create(ast, base.propertyPath, TexSeq(Seq.empty))
    }

    def multicolumn: T = $(_.multicolumn)

    def FloatBarrier: T = $(_.FloatBarrier)
    def centering: T = $(_.centering)
    def toprule: T = $(_.toprule)
    def midrule: T = $(_.midrule)
    def bottomrule: T = $(_.bottomrule)
    def includegraphics: T = $(_.includegraphics)
    def caption: T = $(_.caption)
    def chapter: T = $(_.chapter)
    def section: T = $(_.section)
    def subsection: T = $(_.subsection)
    def subsubsection: T = $(_.subsubsection)
    def item: T = $(_.item)
    def clearpage: T = $(_.clearpage)
    def label: T = $(_.label)
    def ref: T = $(_.ref)
    def usepackage: T = $(_.usepackage)

    def textwidth: T = $(_.textwidth)
    def cmidrule: T = $(_.cmidrule)

    def cite: T = $(_.cite)

    def citep: T = $(_.citep)
    def citet: T = $(_.citet)

    def begin: T = $(_.begin)
    def end: T = $(_.end)

  }

  protected case class TexEnvironment(name: String, sequence: TexSeq = TexSeq(Seq.empty)) {

    def apply(in: TexSeq) = {
      this.copy(sequence = TexSeq(sequence.sequence ++ in.sequence))
    }

    def apply(in: TexOption) = {
      this.copy(sequence = TexSeq(sequence.sequence :+ in))
    }

    def apply(in: String) = {
      this.copy(sequence = TexSeq(sequence.sequence :+ TexValue(in)))
    }

    def apply(in: TexCommandInline) = {
      this.copy(sequence = TexSeq((sequence.sequence :+ TexValue("\\" + in.name)) ++ in.args.sequence))
    }

  }

  protected object TexEnvironmentExpressionBuilder {

    protected val $ = new Query[this.type]

    implicit def query2environment(base: Query[_]): TexEnvironment = {
      TexEnvironment(base.propertyPath.replace("$times", "*"), TexSeq(Seq.empty))
    }

    def itemize: TexEnvironment = $(_.itemize)
    def table: TexEnvironment = $(_.table)
    def figure: TexEnvironment = $(_.figure)
    def center: TexEnvironment = $(_.center)
    def tabularx: TexEnvironment = $(_.tabularx)

  }

  protected object TexEnvironmentBuilder {

    def apply(callback: TexEnvironmentExpressionBuilder.type => TexEnvironment)(block: => Unit) {

      val environment = callback(TexEnvironmentExpressionBuilder)
      val name = environment.name

      \ begin { TexSeq(TexValue(name) +: environment.sequence.sequence) }
      block
      \ end { TexSeq(Seq(TexValue(name))) }
    }

  }

  protected val __\ = {
    new TexCommandExpressionBuilder(ast, TexCommand.apply _) {
      def plain(in: String) = TexPlain(ast, in)
    }
  }
  protected val __\\ = {
    new TexCommandExpressionBuilder(ast, TexCommandInline.apply _)
  }

  def &(in: Int) = TexValue(in.toString())
  def &(in: String) = TexValue(in)
  def &(in: TexOption) = in
  def &(in: TexCommandInline) = in

  def ###(in: String) = TexOption(in)

  def \ = __\
  def \\ = __\\
  def $ = TexEnvironmentBuilder

  def escape(value: String) = {
    TexEscape.escape(value)
  }

}