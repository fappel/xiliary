#!/bin/bash

function error_exit
{
  echo -e "\e[01;31m$1\e[00m" 1>&2
  exit 1
}

set -e

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && ( [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_BRANCH" == "development" ]); then
  # trigger OS-X-build
  echo -e "Trigger OS X build with content from $TRAVIS_BRANCH\n"
  
  # setup git and clone from gh-pages branch
  git checkout "$TRAVIS_BRANCH"
  git config --global user.email "travis-deployer@codeaffine.com"
  git config --global user.name "Travis Deployer"

  # prepare OS X build configuration  
  rm .travis.yml
  mv .travis-os-x.yml .travis.yml
  touch "$TRAVIS_BRANCH"
  git add -A
  git commit -m "Update of OS-X-build branch with latest from $TRAVIS_BRANCH"
  
  # push OS X build configuration commit to OS-X-build brach
  git remote set-url origin https://fappel:${GH_TOKEN}@github.com/fappel/xiliary.git
  git config --global push.default simple
  git push origin HEAD:OS-X-build -f
  
  # clear user/password info
  git remote set-url origin https://xxx:xxx@github.com/fappel/xiliary.git
  echo -e "Done with OS X build trigger\n"
fi
