package com.seanshubin.up_to_date.logic

case class OutOfDate(byPom: Map[String, UpgradeInfo])
