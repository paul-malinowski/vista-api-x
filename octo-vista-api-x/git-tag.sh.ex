#!/bin/sh



. ./build.properties
. production/env.properties


VERSION=$(cat $VERSION_FILE)

VERSION=v$VERSION


cd $SRC

echo "tagging version: $VERSION"

git tag -a $VERSION -m "tagging version $VERSION"
git push origin $VERSION





