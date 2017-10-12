package com.seanshubin.uptodate.logic

trait PomXmlUpgrader {
  def upgrade(text: String, upgrades: Seq[Upgrade]): String
}
