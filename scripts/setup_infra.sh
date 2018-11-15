#!/bin/bash

export IP="$( ./scripts/whats_my_ip.sh )"

./scripts/stop_infra.sh

echo "Building docker"
  pushd docker
  docker-compose build
  docker-compose up -d
  popd
  echo "Waiting 5 seconds for kafka to start"
sleep 5
