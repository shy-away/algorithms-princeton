#!/bin/bash

export CLASSPATH=".:lib/algs4.jar"

TARGET=$1

DIR=$(dirname "$TARGET")
CLASS=$(basename "$TARGET")

javac -cp "$CLASSPATH" "$DIR"/*.java 2>&1 | awk '!/Note/'

shift 1

java -ea -cp "$CLASSPATH:$DIR" "$TARGET" "$@"

find $DIR -name "*.class" -type f -delete