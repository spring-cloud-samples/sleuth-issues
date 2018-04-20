#!/bin/bash

./scripts/stop_infra.sh

# Kill apps
pkill -f "sleuth-issue"