#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

echo Building rapidoid/rapidoid:snapshot

rm -f docker/rapidoid.jar
cp ../docker-rapidoid/entrypoint.sh docker

bin/quick-install.sh
cp rapidoid-standalone/target/rapidoid-standalone-*-SNAPSHOT.jar docker/rapidoid.jar

cd docker
ls -l .

docker build -t rapidoid/rapidoid:snapshot .
docker rmi $(docker images -f "dangling=true" -q)

docker images -a

rm -f entrypoint.sh

echo rapidoid/rapidoid:snapshot was built successfully
