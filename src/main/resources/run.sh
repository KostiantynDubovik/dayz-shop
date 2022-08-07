#!/bin/bash
cd src/main/react || return
git pull
npm run build
cd ../../../
git pull
./gradlew bootRun