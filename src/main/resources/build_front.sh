#!/bin/bash
cd ../react || return
OUTPUT=$(git pull)
echo OUTPUT
if [ OUTPUT != Already*]; then
	npm run build
fi