package com.seanshubin.up_to_date.logic

class RunnerFactoryImpl extends RunnerFactory {
  override def create(validConfiguration: ValidConfiguration): Runner = {
    new Runner() {
      override def run(): Unit = ???
    }
  }
}
