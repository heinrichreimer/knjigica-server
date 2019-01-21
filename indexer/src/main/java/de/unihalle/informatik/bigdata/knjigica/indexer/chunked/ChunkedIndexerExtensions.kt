package de.unihalle.informatik.bigdata.knjigica.indexer.chunked

import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.Indexer

fun <T> Indexer<T>.chunked(chunkSize: Int = ChunkedIndexer.DEFAULT_CHUNK_SIZE): Indexer<T> = ChunkedIndexer(this, chunkSize)