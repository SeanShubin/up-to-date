package com.seanshubin.up_to_date.logic

import java.io.{ByteArrayOutputStream, ByteArrayInputStream, Closeable, InputStream}
import java.nio.charset.Charset
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.w3c.dom.{Document, Node, NodeList}

object DocumentUtil {
  private val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
  private val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()

  def stringToDocument(s: String, charset: Charset): Document = closeAfter(stringToInputStream(s, charset))(inputStreamToDocument)

  def documentToString(document: Document, charset: Charset): String = {
    val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = new DOMSource(document)
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val streamResult = new StreamResult(byteArrayOutputStream)
    transformer.transform(source, streamResult)
    val bytes = byteArrayOutputStream.toByteArray
    val transformedXmlContent = new String(bytes, charset)
    transformedXmlContent
  }

  def nodeListToTraversable(nodeList: NodeList): Traversable[Node] = new Traversable[Node] {
    def foreach[U](f: (Node) => U) {
      for (i <- 0 until nodeList.getLength) yield f(nodeList.item(i))
    }
  }

  def inputStreamToDocument(inputStream: InputStream): Document = documentBuilder.parse(inputStream)

  private def stringToInputStream(s: String, charset: Charset): InputStream = new ByteArrayInputStream(s.getBytes(charset))

  private def closeAfter[ResultType, ClosableType <: Closeable](closable: ClosableType)(block: ClosableType => ResultType): ResultType = {
    val result = try {
      block(closable)
    } finally {
      closable.close()
    }
    result
  }
}
