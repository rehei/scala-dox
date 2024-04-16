package com.github.rehei.scala.dox.control.tex

class TexBuilderEnvironment(markup: TexMarkupFactory) {

  import markup.\

  protected object TexEnvironmentExpressionBuilder {

    protected class SomeTexEnvironment extends TexEnvironment(TexSeq(Seq.empty)) {
      override def name() = {
        this.getClass.getName.replace("$$", "*").stripSuffix("$").split("\\$").last
      }
    }

    protected class NamedEnvironment(name: String, starred: Boolean) extends TexEnvironment(TexSeq(Seq.empty)) {
      override def name() = {
        val suffix = {
          if (starred) { "*" } else { "" }
        }

        name + suffix
      }
    }

    object using {
      def table(starred: Boolean) = {
        new NamedEnvironment("table", starred)
      }
    }


    object Huge extends SomeTexEnvironment
    object itemize extends SomeTexEnvironment
    object table extends SomeTexEnvironment
    object table$ extends SomeTexEnvironment
    object figure extends SomeTexEnvironment
    object mdframed extends SomeTexEnvironment
    object center extends SomeTexEnvironment
    object tabularx extends SomeTexEnvironment
    object tabular extends SomeTexEnvironment
    object tabular$ extends SomeTexEnvironment
    object eqnarray extends SomeTexEnvironment
    object equation extends SomeTexEnvironment
    object align extends SomeTexEnvironment

  }

  def apply(callback: TexEnvironmentExpressionBuilder.type => TexEnvironment)(block: => Unit) {

    val environment = callback(TexEnvironmentExpressionBuilder)
    val name = environment.name

    \ begin { TexSeq(TexValue(name) +: environment.sequence.sequence) }
    block
    \ end { TexSeq(Seq(TexValue(name))) }
  }

}