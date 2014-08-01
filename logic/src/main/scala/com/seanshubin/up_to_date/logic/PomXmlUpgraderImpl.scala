package com.seanshubin.up_to_date.logic

import java.nio.charset.Charset

import org.w3c.dom.Node

class PomXmlUpgraderImpl(charset: Charset,
                         doNotUpgradeFrom: Set[GroupAndArtifact],
                         doNotUpgradeTo: Set[GroupArtifactVersion]) extends PomXmlUpgrader {
  override def upgrade(oldXml: String, upgrades: Map[GroupAndArtifact, String]): String = {
    val document = DocumentUtil.stringToDocument(oldXml, charset)
    val nodeList = document.getElementsByTagName("dependency")
    val nodes = DocumentUtil.nodeListToTraversable(nodeList)
    def upgradeNode(node: Node) {
      val childNodes = DocumentUtil.nodeListToTraversable(node.getChildNodes)
      var groupNode: Option[Node] = None
      var artifactNode: Option[Node] = None
      var versionNode: Option[Node] = None
      def findParts(childNode: Node) {
        if (childNode.getNodeType == Node.ELEMENT_NODE) {
          if (childNode.getNodeName == "groupId") {
            groupNode = Some(childNode)
          } else if (childNode.getNodeName == "artifactId") {
            artifactNode = Some(childNode)
          } else if (childNode.getNodeName == "version") {
            versionNode = Some(childNode)
          }
        }
      }
      childNodes.foreach(findParts)
      (groupNode, artifactNode, versionNode) match {
        case (Some(g), Some(a), Some(v)) =>
          val groupAndArtifact = GroupAndArtifact(g.getTextContent, a.getTextContent)
          upgrades.get(groupAndArtifact) match {
            case Some(toVersion) =>
              val groupArtifactVersion = GroupArtifactVersion(g.getTextContent, a.getTextContent, toVersion)
              if (doNotUpgradeFrom.contains(groupAndArtifact)) {
                //ignore
              } else if (doNotUpgradeTo.contains(groupArtifactVersion)) {
                //ignore
              } else {
                v.setTextContent(toVersion)
              }
            case None =>
          }
        case _ =>
      }
    }
    nodes.foreach(upgradeNode)
    val newXml = DocumentUtil.documentToString(document, charset)
    newXml
  }
}
