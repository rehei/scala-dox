package datax.model.view

import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq

import com.github.rehei.scala.dox.control.xml.XMLMarkupFactory
import com.github.rehei.scala.dox.model.DoxLikeSVG
import com.github.rehei.scala.dox.model.table.DataTable
import com.github.rehei.scala.dox.model.table.DataTableAlignment
import com.github.rehei.scala.dox.model.table.DataTableKeyConfig

class XMLRendering(indexKeyConfig: DataTableKeyConfig) extends XMLMarkupFactory {

  protected val legend = ListBuffer[XMLHeadline]()
  protected val xml = ListBuffer[NodeSeq]()

  def text(in: String) = {
    append(<p>{ in }</p>)
  }
  def svg(image: DoxLikeSVG) = {
    append(image.generateSVG())
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

  def table(model: DataTable) = {

    model.withIndex(Some(indexKeyConfig))

    append {
      <table style="width: 600px; margin: auto; table-layout: fixed;" class="table table-striped table-sm">
        <thead>
          {
            for (element <- model.head) yield {
              <th style={ getStyle(element) }>{ element.name }</th>
            }
          }
        </thead>
        <tbody>
          {
            for (row <- model.data) yield {
              <tr>
                {
                  for ((element, index) <- row.zipWithIndex) yield {
                    <td style={ getStyle(model.head(index)) }>{ element }</td>
                  }
                }
              </tr>
            }
          }
        </tbody>
      </table>
    }
  }

  def generateHead() = {
    legend.map(_.renderHead())
  }

  def generateBody() = {
    xml.flatten
  }

  protected def getStyle(config: DataTableKeyConfig) = {

    config.alignment match {
      case DataTableAlignment.LEFT  => "text-align: left;"
      case DataTableAlignment.RIGHT => "text-align: right;"
      case _                        => "text-align: center;"
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