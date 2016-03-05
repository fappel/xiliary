#!/bin/bash

function error_exit
{
  echo -e "\e[01;31m$1\e[00m" 1>&2
  exit 1
}

set -e

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && ( [ -f "master" ] || [ -f "development" ]); then
  echo -e "Checkout composite repository from gh-pages\n"
  cwd=$(pwd)
  
  # create and cd into temporary deployment work directory
  mkdir $HOME/deployment-work
  cd $HOME/deployment-work

  # setup git and clone from gh-pages branch
  git config --global user.email "travis-deployer@codeaffine.com"
  git config --global user.name "Travis Deployer"
  git clone --quiet --branch=gh-pages https://fappel:${GH_TOKEN}@github.com/fappel/xiliary.git . > /dev/null 2>&1 || error_exit "Error cloning gh-pages"

  # remove web content
  if [ -f "master" ]; then
    export DEPLOY_WORK_DIRECTORY=""
    rm *.html
    rm -rf ./assets
    rm -rf ./images
    rm -rf ./development
  fi
  if [ -f "development" ]; then
    cd development
    export DEPLOY_WORK_DIRECTORY="development"
    rm *.html
    rm -rf ./assets
    rm -rf ./images
    rm -rf ./development
  fi

  # go back to the directory where we started
  cd $cwd

  echo -e "Done with composite repository deployment preparations\n"
fi
