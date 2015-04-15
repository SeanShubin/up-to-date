package com.seanshubin.up_to_date.logic

import java.nio.charset.Charset

import org.w3c.dom.{Element, Node, NodeList}

class PomParserImpl(charset: Charset) extends PomParser {
  override def parseDependencies(pomName: String, pomContents: String): Pom = {
    val document = DocumentUtil.stringToDocument(pomContents, charset)
    val nodeList = document.getElementsByTagName("dependency")
    val traversableNodeList: Traversable[Node] = nodeListToTraversable(nodeList)
    val nodeToDependency: Node => Option[Dependency] = pomNameAndNodeToDependency(pomName, _: Node)
    val dependencies = traversableNodeList.map(nodeToDependency).toSeq.flatten
    val pom = Pom(pomName, dependencies)
    pom
  }

  private def pomNameAndNodeToDependency(pomName: String, node: Node): Option[Dependency] = {
    val childNodeList = node.getChildNodes
    val childNodes = nodeListToTraversable(childNodeList)
    val childNodeMapEntries = for {
      childNode <- childNodes
      if childNode.getNodeType == Node.ELEMENT_NODE
      element = childNode.asInstanceOf[Element]
    } yield {
        (element.getNodeName, element.getTextContent)
      }
    val childNodeMap = childNodeMapEntries.toMap
    if (hasNecessaryFields(childNodeMap)) Some(pomNameAndFieldsToDependency(pomName, childNodeMap))
    else None
  }

  private def nodeListToTraversable(nodeList: NodeList): Traversable[Node] = new Traversable[Node] {
    def foreach[U](f: (Node) => U) {
      for (i <- 0 until nodeList.getLength) yield f(nodeList.item(i))
    }
  }

  private def hasNecessaryFields(fields: Map[String, String]): Boolean = {
    def containsValid(name: String): Boolean = fields.contains(name) && !fields(name).startsWith("$")
    containsValid("groupId") && containsValid("artifactId") && containsValid("version")
  }

  private def pomNameAndFieldsToDependency(pomName: String, fields: Map[String, String]): Dependency = {
    val dependency = Dependency(pomName, fields("groupId"), fields("artifactId"), fields("version"))
    dependency
  }
}
