#!/usr/bin/env bash

# If not installed install JDK/JRE < 9

# Download ElasticSearch.
curl -L -O https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.4.6/elasticsearch-2.4.6.tar.gz
# Extract downloaded sources.
tar -xvf elasticsearch-2.4.6.tar.gz
