#!/bin/bash

# extract algs4.jar
mkdir algs4_decompressed
cd algs4_decompressed
jar xf ../algs4.jar > log.txt 2>&1

# generate JavaDocs
cd ..
javadoc -d docs algs4_decompressed/edu/princeton/cs/algs4/*.java > log.txt 2>&1