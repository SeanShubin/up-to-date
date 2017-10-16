package com.seanshubin.uptodate.logic

class RunnerImpl(flow:Flow) extends Runnable {
  override def run(): Unit = {
    flow.run()
  }
}
