package com.github.rehei.scala.dox.model.bibliography

import org.jbibtex.BibTeXFormatter
import org.jbibtex.BibTeXEntry
import java.io.Writer
import java.util.Map.Entry
import org.jbibtex.Key
import org.jbibtex.Value
import scala.collection.JavaConversions._
import org.jbibtex.BibTeXDatabase
import java.io.StringWriter
import scala.collection.Seq

case class DoxBibtexFormat(keyOverride: String) {

  case class Format() extends BibTeXFormatter() {

    protected val KEY_BLACKLIST = Seq(BibTeXEntry.KEY_URL)
    
    override protected def format(entry: BibTeXEntry, writer: Writer) {
      writer.write("@");
      format(entry.getType(), writer);

      writer.write('{');
      formatIndexKey(entry.getKey(), writer)
      writer.write(',');
      writer.write('\n');

      val iteration = entry.getFields().entrySet().iterator()

      for (entry <- iteration) {
        formatEntry(entry, writer, iteration.hasNext())
      }

      writer.write('}');
    }

    protected def formatIndexKey(input: Key, writer: Writer) {
      writer.write(keyOverride)
    }

    protected def formatEntry(entry: Entry[Key, Value], writer: Writer, hasNext: Boolean) {
      if (KEY_BLACKLIST.contains(entry.getKey)) {
        // ignore
      } else {
        writer.write(getIndent());
        format(entry.getKey(), writer);
        writer.write(" = ");
        format(entry.getValue(), 2, writer);
        if (hasNext) {
          writer.write(',');
        }
        writer.write('\n');
      }
    }

  }

  def format(database: BibTeXDatabase) = {
    val writer = new StringWriter()
    Format().format(database, writer)
    writer.append("\n")
    writer.toString()
  }

}
