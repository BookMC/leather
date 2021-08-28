package org.bookmc.tannery.utils

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun createRunConfig(
    projectDir: File,
    mainClass: String,
    libraryFolder: File,
    gameVersion: String,
    gameJar: File,
    mappingsDirectory: File,
    projectName: String,
    sourceSet: String
) {
    val factory = DocumentBuilderFactory.newInstance()
    val documentBuilder = factory.newDocumentBuilder()
    val document = documentBuilder.parse(File(projectDir, INTELLIJ_WORKSPACE))
    val rootNode = document.getElementsByTagName(PROJECT).item(0)
    val rootNodeChildren = rootNode.childNodes

    for (i in 0 until rootNodeChildren.length) {
        val projectNodeChild = rootNodeChildren.item(i)
        val nodeName = projectNodeChild.nodeName
        if (nodeName == COMPONENT) {
            val nodeAttributeName = projectNodeChild
                .attributes
                .getNamedItem(NAME)
                .textContent
            if (nodeAttributeName == RUN_MANAGER) {
                val elementMap = HashMap<String, String>()

                // Name
                elementMap[NAME] = RUN_CONFIG_NAME.format(gameVersion)
                elementMap[TYPE] = RUN_CONFIG_TYPE
                elementMap[FACTORY_NAME] = RUN_CONFIG_TYPE
                elementMap[NAME_IS_GENERATED] = TRUE
                val element = generateConfiguration(document, elementMap)

                // Main Class Option
                val mainClassOptions = HashMap<String, String>()
                mainClassOptions[NAME] = MAIN_CLASS_NAME
                mainClassOptions[VALUE] = mainClass
                element.appendChild(generateOption(document, mainClassOptions))

                val moduleOptions = HashMap<String, String>()
                moduleOptions[NAME] = "$projectName.$sourceSet"
                element.appendChild(generateElement(document, "module", moduleOptions))

                // Program Arguments
                val programArguments = HashMap<String, String>()
                programArguments[NAME] = PROGRAM_PARAMETERS
                programArguments[VALUE] = listOf(
                    "--library-folder",
                    libraryFolder.absolutePath,
                    "--jar",
                    gameJar.absolutePath,
                    "--mappings",
                    mappingsDirectory.absolutePath
                ).joinToString(" ")

                element.appendChild(generateOption(document, programArguments))
                val methodOptions = HashMap<String, String>()
                methodOptions["v"] = "2"
                val method = generateMethod(document, methodOptions)
                val make = HashMap<String, String>()
                make[NAME] = INTELLIJ_MAKE
                make[ENABLED] = TRUE
                method.appendChild(generateOption(document, make))
                projectNodeChild.appendChild(element)

                var listNode: Node? = null
                for (i1 in 0 until projectNodeChild.childNodes.length) {
                    val node = projectNodeChild.childNodes.item(i1)
                    if (node.nodeName.equals(LIST)) {
                        listNode = node
                        break
                    }
                }

                if (listNode == null) {
                    listNode = document.createElement("list")
                }

                listNode?.appendChild(
                    generateListItem(
                        document,
                        "$RUN_CONFIG_TYPE.$RUN_CONFIG_NAME".format(gameVersion)
                    )
                )


                val transformerFactory = TransformerFactory.newInstance()
                val transformer = transformerFactory.newTransformer()
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, NO)
                transformer.setOutputProperty(OutputKeys.METHOD, XML)
                transformer.setOutputProperty(OutputKeys.INDENT, YES)
                transformer.setOutputProperty(OutputKeys.ENCODING, UTF8)
                transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "4")
                val source = DOMSource(document)
                val result = StreamResult(File(projectDir, INTELLIJ_WORKSPACE))
                transformer.transform(source, result)
                break
            }
        }
    }
}

private fun generateConfiguration(document: Document, attributes: Map<String, String>): Element {
    return generateElement(document, INTELLIJ_CONFIGURATION, attributes)
}

private fun generateOption(document: Document, attributes: Map<String, String>): Element {
    return generateElement(document, INTELLIJ_OPTION, attributes)
}

private fun generateMethod(document: Document, attributes: Map<String, String>): Element {
    return generateElement(document, INTELLIJ_METHOD, attributes)
}

private fun generateListItem(document: Document, value: String): Element {
    val map = HashMap<String, String>()
    map["itemvalue"] = value
    return generateElement(document, ITEM, map)
}

private fun generateElement(document: Document, tagName: String, attributes: Map<String, String>): Element {
    val element = document.createElement(tagName)
    for ((key, value) in attributes) {
        element.setAttribute(key, value)
    }
    return element
}