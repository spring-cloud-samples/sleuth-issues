#!/bin/bash

set -o errexit

./mvnw clean install -fae -U
