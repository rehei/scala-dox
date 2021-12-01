package com.github.rehei.scala.dox.control.xml

import scala.xml.NodeSeq
import com.github.rehei.scala.dox.util.NextID

trait XMLMarkupFactory {

  case class XMLHeadline(someID: String, name: String, multiply: Int, callback: XMLHeadline => NodeSeq) {

    def id = { someID }

    def renderHead() = {

      <span>
        { "\u00A0" * 4 * { multiply - 1 } }
        <a href={ "#" + someID }>
          <strong>{ name }</strong><br style={ "clear: both" }/>
        </a>
      </span>
    }

    def renderBody(): NodeSeq = {
      callback(this)
    }

  }

  protected val someID = NextID("id")

  def H1(name: String) = XMLHeadline(someID.nextID(), name, 1, headline => <h1 id={ headline.id }>{ headline.name }</h1>)
  def H2(name: String) = XMLHeadline(someID.nextID(), name, 2, headline => <h2 id={ headline.id }>{ headline.name }</h2>)
  def H3(name: String) = XMLHeadline(someID.nextID(), name, 3, headline => <h3 id={ headline.id }>{ headline.name }</h3>)
  def H4(name: String) = XMLHeadline(someID.nextID(), name, 4, headline => <h4 id={ headline.id }>{ headline.name }</h4>)

}