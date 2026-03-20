#!/bin/bash

export CLASSPATH=".:lib/algs4.jar"

TARGET=$1

DIR=$(dirname "$TARGET")
CLASS=$(basename "$TARGET")

javac -cp "$CLASSPATH" "$TARGET"

shift 1

java -cp "$CLASSPATH:$DIR" "$TARGET" "$@"