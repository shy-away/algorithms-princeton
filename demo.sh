#!/bin/bash

USAGE="Usage: $0 [-i] <target_class>"

export CLASSPATH=".:lib/algs4.jar"

INTERPRET_FLAG=""

while getopts "i" opt; do
  case $opt in
    i) INTERPRET_FLAG="-Xint";;
    *) echo "$USAGE" >&2; exit 1;;
  esac
done


shift $((OPTIND-1))

if [ $# -lt 1 ]; then
  echo "$USAGE" >&2
  exit 1
fi

TARGET=$1
DIR=$(dirname "$TARGET")

javac -cp "$CLASSPATH" "$DIR"/*.java 2>&1 | awk '!/Note/'

shift 1

java -ea $INTERPRET_FLAG -cp "$CLASSPATH:$DIR" "$TARGET" "$@"

find "$DIR" -name "*.class" -type f -delete