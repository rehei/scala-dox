package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

import com.github.rehei.scala.dox.model.table.DoxTableKeyCategory
import com.github.rehei.scala.macros.Query

case class DoxTableFactoryKeySelection_test[T](query: Query[T]) {
  protected val keyBuffer = ListBuffer[DoxTableFactoryKey_test]()

  def take(property: Query[T] => Query[_]) = new {
    def config(config: DoxTableKeyConfig_test) = {
      DoxTableFactoryKey_test(property(query), config)
    }
  }

  def getKeys() = {
    keyBuffer.seq
  }
//  def forCategory(category: DoxTableKeyCategory) = new {
//    
//    def take(property: Query[T] => Query[_]) = new {
//      def config(config: DoxTableKeyConfig) = {
//        DoxTableFactoryKey(property(query), config)
//      }
//    }
//  }
    def forCategory(category: DoxTableKeyCategory) = new DoxTableKeys { }
    
  //  def takeWithChildren(property: Query[T] => Query[_]) = new {
  //    property(query).
  //    def config(config: DoxTableKeyConfig) = {
  //      DoxTableFactoryKey(property(query), config)
  //    }
  //    def
  //  }
  abstract class DoxTableKeys() {
    protected val keyBuffer = ListBuffer[DoxTableFactoryKey_test]()
    //    def take(property: Query[T] => Query[_]):Unit = {
    //      def config(config: DoxTableKeyConfig): DoxTableKeys
    //    }

    protected val instance = this

        def take(property: Query[T] => Query[_]) = new {
    
          def config(config: DoxTableKeyConfig_test) = {
    
            keyBuffer.append(DoxTableFactoryKey_test(property(query), config))
            instance
          }
        }
        def key() = {
          
//          keyBuffer.map(_.)
        }
  }
}