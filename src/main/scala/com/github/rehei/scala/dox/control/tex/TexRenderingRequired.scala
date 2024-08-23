package com.github.rehei.scala.dox.control.tex

class TexRenderingRequired(baseAST: TexAST) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  def render() = {
    requireFloat()
    requireText()
    requireMath()
    requireTable()
  }

  protected def requireFloat() = {

    \ usepackage { "float" }
    \ usepackage { "placeins" }

  }

  protected def requireText() = {

    \ usepackage & { ###("T1") } { "fontenc" }
    \ usepackage & { ###("utf8x") } { "inputenc" }
    \ usepackage & { "textcomp" }

  }

  protected def requireMath() = {

    \ usepackage { "amsmath" }
    \ usepackage { "amssymb" }
    \ usepackage { "wasysym" }
    \ usepackage { "graphicx" }
    
  }

  protected def requireTable() = {

    \ usepackage { "mdframed" }
    \ usepackage { "tabularx" }
    \ usepackage { "multirow" }
    \ usepackage { "array" }
    \ usepackage { "siunitx" }

    \ plain {
      """
        \restylefloat{table}
        \newcommand{\columnBoxFramed}[2]{\setlength{\fboxsep}{0pt}\setlength{\fboxrule}{0.1pt}\fbox{\begin{minipage}[t]{#1}#2\end{minipage}}}
        \newcommand{\columnBox}[2]{\setlength{\fboxsep}{0.1pt}\setlength{\fboxrule}{0pt}\fbox{\begin{minipage}[t]{#1}#2\end{minipage}}}
        \setlength\heavyrulewidth{1pt}
      """
    }
  }

}