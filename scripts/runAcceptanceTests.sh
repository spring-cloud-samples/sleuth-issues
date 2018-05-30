#!/bin/bash

set -o errexit

trap './scripts/cleanup.sh' EXIT

./scripts/setup_infra.sh

./mvnw clean install -fae -U


