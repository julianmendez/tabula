#!/bin/bash

cd `dirname $0`
date +%FT%T
echo `basename $0`


cd ../../..

mvn exec:java

date +%FT%T


