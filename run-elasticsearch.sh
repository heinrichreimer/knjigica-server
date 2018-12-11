#!/usr/bin/env bash

docker run -p 9200:9200 -e "discovery.type=single-node"