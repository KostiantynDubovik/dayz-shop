#!/bin/bash
cd ../react || return
OUTPUT=$(git pull)
echo OUTPUT
if [ OUTPUT != "Already up to date."]; then
	npm run build
fi