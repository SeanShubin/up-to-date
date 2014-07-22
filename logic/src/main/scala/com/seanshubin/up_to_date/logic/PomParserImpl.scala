package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import org.w3c.dom.{Element, Node, NodeList}

class PomParserImpl(fileSystem: FileSystem) extends PomParser {
  override def parseDependencies(path: Path): (String, Seq[PomDependency]) = {
    val document = DocumentUtil.inputStreamToDocument(fileSystem.pathToInputStream(path))
    val nodeList = document.getElementsByTagName("dependency")
    val traversableNodeList: Traversable[Node] = nodeListToTraversable(nodeList)
    val nodeToDependency: Node => Option[PomDependency] = pathAndNodeToDependency(path, _: Node)
    val dependencies = traversableNodeList.map(nodeToDependency).toSeq.flatten
    (path.toString, dependencies)
  }

  private def nodeListToTraversable(nodeList: NodeList): Traversable[Node] = new Traversable[Node] {
    def foreach[U](f: (Node) => U) {
      for (i <- 0 until nodeList.getLength) yield f(nodeList.item(i))
    }
  }

  private def pathAndNodeToDependency(path: Path, node: Node): Option[PomDependency] = {
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
    if (hasNecessaryFields(childNodeMap)) Some(pathAndFieldsToDependency(path, childNodeMap))
    else None
  }

  private def hasNecessaryFields(fields: Map[String, String]): Boolean = {
    def containsValid(name: String): Boolean = fields.contains(name) && !fields(name).startsWith("$")
    containsValid("groupId") && containsValid("artifactId") && containsValid("version")
  }

  private def pathAndFieldsToDependency(path: Path, fields: Map[String, String]): PomDependency = {
    val dependency = PomDependency(fields("groupId"), fields("artifactId"), fields("version"))
    dependency
  }
}
