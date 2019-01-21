package de.unihalle.informatik.bigdata.knjigica.indexer

sealed class IndexConfiguration(
        val index: String,
        val type: String
) {
    object Annotation : IndexConfiguration("annotations", "annotation")
    object Author : IndexConfiguration("authors", "author")
    object Opera : IndexConfiguration("operas", "opera")
    object Plot : IndexConfiguration("plots", "plot")
    object Role : IndexConfiguration("roles", "role")
}