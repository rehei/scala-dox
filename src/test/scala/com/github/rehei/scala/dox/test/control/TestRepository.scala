package com.github.rehei.scala.dox.test.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum

object TestRepository {

  object TestRepositoryKeyFinal extends DoxBibKeyEnum {

    var variableKey = fromRAW("")

  }

  object TestRepositoryKeyValidation extends TestRepositoryKeyValidation

  object TestRepositoryKeyValidationNested extends DoxBibKeyEnum {

    object foo {
      val reference = fromRAW("")
    }

  }

  object TestRepositorySourceObjectRequirement extends TestRepositorySourceObjectRequirement

  class TestRepositoryKeyValidation extends DoxBibKeyEnum {

    val reference1 = fromRAW("")

  }

  class TestRepositorySourceObjectRequirement extends DoxBibKeyEnum {

  }

  object TestAggregated extends DoxBibKeyEnum {

    val reference1 = fromRAW("")

    object Inner extends DoxBibKeyEnum {

      val reference2 = fromRAW((""))

    }

  }

}