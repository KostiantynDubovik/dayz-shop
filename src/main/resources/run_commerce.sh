#!/bin/bash
DEBUG=''
case $1 in
'd')
  DEBUG='--debug-jvm'
  ;;
esac
curl --request GET -sL -o nul\
			--max-time 30 \
			--url 'https://localhost/shutdown'
cd ../react/voyage || return
git pull
npm run build
cd ../../../../
git pull
./gradlew clean
./gradlew bootRun $DEBUG