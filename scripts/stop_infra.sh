#!/bin/bash

pushd docker
  docker-compose kill || echo "Failed to kill docker"
  yes | docker-compose rm -v || echo "Failed to remove volumes"
  docker stop $(docker ps | grep zookeeper| awk -F' ' '{print $1}') || echo "Failed to stop zookeeper"
  docker stop $(docker ps | grep kafka | awk -F' ' '{print $1}') || echo "Failed to stop kafka"
popd
