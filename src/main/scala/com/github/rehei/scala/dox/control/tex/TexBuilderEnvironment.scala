package com.github.rehei.scala.dox.control.tex

class TexBuilderEnvironment(markup: TexMarkupFactory) {

  import markup.\
  
  protected object TexEnvironmentExpressionBuilder {

    protected class SomeTexEnvironment extends TexEnvironment(TexSeq(Seq.empty)) {
      override def name() = {
        this.getClass.getName.stripSuffix("$").split("\\$").last
      }
    }

    object itemize extends SomeTexEnvironment
    object table extends SomeTexEnvironment
    object figure extends SomeTexEnvironment
    object center extends SomeTexEnvironment
    object tabularx extends SomeTexEnvironment
    object eqnarray extends SomeTexEnvironment

  }

  def apply(callback: TexEnvironmentExpressionBuilder.type => TexEnvironment)(block: => Unit) {

    val environment = callback(TexEnvironmentExpressionBuilder)
    val name = environment.name

    \ begin { TexSeq(TexValue(name) +: environment.sequence.sequence) }
    block
    \ end { TexSeq(Seq(TexValue(name))) }
  }

}