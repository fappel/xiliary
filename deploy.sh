#!/bin/bash

function error_exit
{
  echo -e "\e[01;31m$1\e[00m" 1>&2
  exit 1
}

set -e

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && ( [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_BRANCH" == "development" ]); then
  echo -e "Starting to deploy to gh-pages\n"
  cwd=$(pwd)

  # cd into temporary deployment work directory
  cd $HOME/deployment-work

  # add, commit and push files
  git add -f -A
  git commit -m "[ci skip] Deploy Travis build #$TRAVIS_BUILD_NUMBER to gh-pages"
  git push -fq origin gh-pages > /dev/null 2>&1 || error_exit "Error uploading the build result to gh-pages"

  # go back to the directory where we started
  cd $cwd

  echo -e "Done with deployment to gh-pages\n"
fi
