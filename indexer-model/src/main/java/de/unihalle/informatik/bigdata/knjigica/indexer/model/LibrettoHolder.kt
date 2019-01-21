package de.unihalle.informatik.bigdata.knjigica.indexer.model

data class LibrettoHolder(
        val annotations: Sequence<Annotation>,
        val authors: Sequence<Author>,
        val operas: Sequence<Opera>,
        val plot: Sequence<Plot>,
        val roles: Sequence<Role>
)