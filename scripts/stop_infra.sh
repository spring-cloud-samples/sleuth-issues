#!/bin/bash

pushd docker
docker-compose kill || echo "Failed to kill docker"
yes | docker-compose rm -v || echo "Failed to remove volumes"
popd
