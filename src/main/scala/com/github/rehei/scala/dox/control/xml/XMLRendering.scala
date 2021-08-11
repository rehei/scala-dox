package com.github.rehei.scala.dox.control.xml

import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2XML

class XMLRendering(indexKeyConfig: DoxTableKeyConfig) extends XMLMarkupFactory {

  protected val legend = ListBuffer[XMLHeadline]()
  protected val xml = ListBuffer[NodeSeq]()

  def text(in: String) = {
    append(<p>{ in }</p>)
  }
  def svg(image: NodeSeq) = {
    append(image)
  }
  def chapter(name: String) = {
    append { H1(name) }
  }
  def section(name: String) = {
    append { H2(name) }
  }
  def subsection(name: String) = {
    append { H3(name) }
  }
  def subsubsection(name: String) = {
    append { H4(name) }
  }

  def generateHead() = {
    legend.map(_.renderHead())
  }

  def generateBody() = {
    xml.flatten
  }

  protected def getStyle(config: DoxTableKeyConfig) = {

    config.alignment match {
      case DoxTableAlignment.LEFT  => "text-align: left;"
      case DoxTableAlignment.RIGHT => "text-align: right;"
      case _                       => "text-align: center;"
    }

  }

  protected def append(headline: XMLHeadline): this.type = {

    legend.append(headline)

    append(headline.renderBody())

    this
  }

  protected def append(in: NodeSeq): this.type = {
    xml.append(in)
    xml.append(<br style="clear: both;"/>)
    xml.append(<br style="clear: both;"/>)

    this
  }

}