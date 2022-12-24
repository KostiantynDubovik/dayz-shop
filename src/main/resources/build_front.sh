#!/bin/bash
cd ../react || return
OUTPUT=$(git pull)
if [ OUTPUT != "Already up to date."]; then
	npm run build
fi