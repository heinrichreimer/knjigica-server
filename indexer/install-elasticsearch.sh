#!/usr/bin/env bash

# If not installed install JDK/JRE < 9
ELASTIC_VERSION=6.5.1

# Download ElasticSearch.
curl -L -O "https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-$ELASTIC_VERSION.tar.gz"
# Extract downloaded sources.
tar -xvf "elasticsearch-$ELASTIC_VERSION.tar.gz"
