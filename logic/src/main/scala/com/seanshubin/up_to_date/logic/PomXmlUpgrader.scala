package com.seanshubin.up_to_date.logic

trait PomXmlUpgrader {
  def upgrade(text: String, upgrades: Seq[Upgrade]): String
}
