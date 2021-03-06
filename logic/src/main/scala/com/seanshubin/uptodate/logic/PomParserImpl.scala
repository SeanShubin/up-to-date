package com.seanshubin.uptodate.logic

import java.nio.charset.Charset

import org.w3c.dom.{Element, Node, NodeList}

class PomParserImpl(charset: Charset) extends PomParser {
  override def parseDependencies(pomName: String, pomContents: String): Pom = {
    val document = DocumentUtil.stringToDocument(pomContents, charset)
    val dependencyList = document.getElementsByTagName("dependency")
    val propertyList = document.getElementsByTagName("properties")
    val traversableDependencyList: Traversable[Node] = nodeListToTraversable(dependencyList)
    val traversableProperties: Traversable[Node] = nodeListToTraversable(propertyList)
    val nodeToDependency: Node => Option[Dependency] = pomNameAndNodeToDependency(pomName, _: Node)
    val dependencies = traversableDependencyList.map(nodeToDependency).toSeq.flatten
    val properties = traversableProperties.flatMap(propertiesToMap).toMap
    val pom = Pom(pomName, dependencies, properties)
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

  private def propertiesToMap(node: Node): Map[String, String] = {
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
    childNodeMap
  }

  private def nodeListToTraversable(nodeList: NodeList): Traversable[Node] = new Traversable[Node] {
    def foreach[U](f: (Node) => U) {
      for (i <- 0 until nodeList.getLength) yield f(nodeList.item(i))
    }
  }

  private def hasNecessaryFields(fields: Map[String, String]): Boolean = {
    def containsValid(name: String): Boolean = fields.contains(name)

    containsValid("groupId") && containsValid("artifactId") && containsValid("version")
  }

  private def pomNameAndFieldsToDependency(pomName: String, fields: Map[String, String]): Dependency = {
    val dependency = Dependency(pomName, fields("groupId"), fields("artifactId"), fields("version"))
    dependency
  }
}
