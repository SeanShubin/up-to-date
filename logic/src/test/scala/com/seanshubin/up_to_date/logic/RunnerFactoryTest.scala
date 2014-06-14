package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerFactoryTest extends FunSuite with EasyMockSugar {
  test("create runner factory") {
    val validConfiguration = SampleData.validConfiguration
    val runnerFactory: RunnerFactory = new RunnerFactoryImpl
    val runner: Runner = runnerFactory.create(validConfiguration)
    assert(runner !== null)
  }
}
