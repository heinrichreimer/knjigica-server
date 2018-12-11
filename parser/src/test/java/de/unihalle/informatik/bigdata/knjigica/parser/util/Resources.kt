package de.unihalle.informatik.bigdata.knjigica.parser.util

import okio.source
import java.io.File
import java.net.URL

fun <T : Any> T.getResourceURL(name: String): URL? = javaClass.classLoader.getResource(name)
fun <T : Any> T.getResourceFile(name: String): File? = getResourceURL(name)?.toURI()?.let(::File)
fun <T : Any> T.getResourceStream(name: String) = javaClass.classLoader.getResourceAsStream(name)!!
fun <T : Any> T.getResourceSource(name: String) = getResourceStream(name).source()