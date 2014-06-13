package com.seanshubin.up_to_date.logic

trait RunnerFactory {
  def create(validConfiguration: ValidConfiguration): Runner
}
