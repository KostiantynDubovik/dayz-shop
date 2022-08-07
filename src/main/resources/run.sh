#!/bin/bash
curl --request GET -sL -o nul\
			--max-time 30 \
			--url 'http://root.dayz-shop.com/shutdown'
cd src/main/react || return
git pull
npm run build
cd ../../../
git pull
./gradlew bootRun