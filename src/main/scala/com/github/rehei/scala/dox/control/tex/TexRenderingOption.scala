package com.github.rehei.scala.dox.control.tex

class TexRenderingOption(baseAST: TexAST) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  def showFrame(enable: Boolean) {
    if (enable) {
      \ usepackage { "showframe" }
    }
  }

  def showFrameColumn(enable: Boolean) {
    
    /*
     * See https://tex.stackexchange.com/questions/271285/show-frame-margin-in-two-column-layout 
     */
    
    if (enable) {
      \ usepackage { "etoolbox" }

      \ plain {
        """
        \newlength\Fcolumnseprule
        \setlength\Fcolumnseprule{0.4pt}
        
        \makeatletter
        \newcommand\ShowInterColumnFrame{
        \patchcmd{\@outputdblcol}
          {{\normalcolor\vrule \@width\columnseprule}}
          {\vrule \@width\Fcolumnseprule\hfil
            {\normalcolor\vrule \@width\columnseprule}
            \hfil\vrule \@width\Fcolumnseprule
          }
          {}
          {}
        }
        \makeatother
        
        \ShowInterColumnFrame
        """
      }

    }
  }

}