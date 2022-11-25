#!/bin/bash
debug = ''
case $1 in
'd')
  debug = '--debug-jvm'
  ;;
esac
curl --request GET -sL -o nul\
			--max-time 30 \
			--url 'http://root.dayz-shop.com/shutdown'
cd ../react || return
git pull
npm run build
cd ../../../
git pull
./gradlew clean
./gradlew bootRun $debug