#!/bin/bash

function error_exit
{
  echo -e "\e[01;31m$1\e[00m" 1>&2
  exit 1
}

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && ( [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_BRANCH" == "development" ]); then
  echo -e "Starting to deploy to gh-pages\n"

  # create and cd into temporary deployment work directory
  mkdir deployment-work
  cd deployment-work
  
  # setup git and clone from gh-pages branch
  git config --global user.email "travis-deployer@codeaffine.com"
  git config --global user.name "Travis Deployer"
  git clone --quiet --branch=gh-pages https://fappel:${GH_TOKEN}@github.com/fappel/xiliary.git . > /dev/null 2>&1 || error_exit "Error cloning gh-pages"


  # clean the repository directory, then copy the build result into it
  if [ "$TRAVIS_BRANCH" == "master" ]; then
    git rm -rf ./*
    cp -rf ../com.codeaffine.xiliary.releng/repository/target/repository/* ./
  fi
  
  if [ "$TRAVIS_BRANCH" == "development" ]; then
    if [ ! -d development ]; then
      mkdir development
    fi
    cd development
    git rm -rf ./*
    cp -rf ../../com.codeaffine.xiliary.releng/repository/target/repository/* ./  
  fi
  
  # add, commit and push files
  git add -f .
  git commit -m "[ci skip] Deploy Travis build #$TRAVIS_BUILD_NUMBER to gh-pages"
  git push -fq origin gh-pages > /dev/null 2>&1 || error_exit "Error uploading the build result to gh-pages"

  # go back to the directory where we started
  cd ..
  
  echo -e "Done with deployment to gh-pages\n"
fi
