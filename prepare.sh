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
  
  # create temporary deployment work directory
  mkdir $HOME/deployment-work

  # setup git and clone gh-pages branch into deployment work directory
  git config --global user.email "travis-deployer@codeaffine.com"
  git config --global user.name "Travis Deployer"
  git clone --branch=gh-pages https://fappel:${GH_TOKEN}@github.com/fappel/xiliary.git $HOME/deployment-work > /dev/null 2>&1 || error_exit "Error cloning gh-pages"

  # remove web content
  if [ -f "master" ]; then
    echo -e "Prepare deployment of branch master\n"
    export DEPLOY_WORK_DIRECTORY=""
    # cd does not work with set -e
    set +e 
    cd $HOME/deployment-work
    set -e 
  fi
  if [ -f "development" ]; then
    echo -e "Prepare deployment of branch development\n"
    export DEPLOY_WORK_DIRECTORY="development"
    # cd does not work with set -e
    set +e 
    cd $HOME/deployment-work/development
    set -e 
  fi
  
  echo -e "Build deployment directory is '$DEPLOY_WORK_DIRECTORY'"
  echo -e "Working directory is '$(pwd)'"
  rm -f *.html
  echo -e "removed html resources"
  rm -rf ./assets
  echo -e "cleared assets folder"
  rm -rf ./images
  echo -e "cleared images folder"    
  rm -rf ./development
  echo -e "cleared development repository content"

  # go back to the directory where we started
  # cd does not work with set -e
  set +e 
  cd $cwd
  echo -e "Working directory is '$(pwd)'"
  set -e 

  echo -e "\nDone with composite repository deployment preparations\n"
fi
