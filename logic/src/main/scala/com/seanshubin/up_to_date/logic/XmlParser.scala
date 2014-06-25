package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import org.w3c.dom.Document

trait XmlParser {
  def parse(path: Path): Document
}
