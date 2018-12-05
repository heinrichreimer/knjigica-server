#!/usr/bin/env bash

# Download Kareol corpus.
docker run -p 1358:1358 -d appbaseio/dejavu
open http://localhost:1358/
