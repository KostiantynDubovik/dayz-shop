#!/bin/bash
cd src/main/react || return
npm run build
cd ../../../
./gradlew bootRun