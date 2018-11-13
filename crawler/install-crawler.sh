#!/usr/bin/env bash

# Install HTML parser for bash.
sudo apt install golang-go
export GOPATH="$(go env GOPATH)"
export PATH="$PATH:$GOPATH/bin"
go get github.com/ericchiang/pup
