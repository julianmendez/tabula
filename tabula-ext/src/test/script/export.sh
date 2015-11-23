#!/bin/bash

cd `dirname $0`
date +%FT%T
echo `basename $0`

cd ../../..

mvn exec:java --quiet -Dexec.args="simple src/test/resources/example.properties target/example.properties"
mvn exec:java --quiet -Dexec.args="wikitext src/test/resources/example.properties target/example.text"
mvn exec:java --quiet -Dexec.args="html src/test/resources/example.properties target/example.html"

date +%FT%T


