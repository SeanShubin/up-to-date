package com.seanshubin.up_to_date.logic

trait RunnerFactory {
  def createRunner(configuration: Configuration): Runner
}
