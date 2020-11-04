#!/bin/bash

./scripts/stop_infra.sh

# Kill apps
pkill -f "sleuth-issue" || echo "Failed to kill any running apps"
