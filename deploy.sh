!/bin/bash

function error_exit
{
  echo -e "\e[01;31m$1\e[00m" 1>&2
  exit 1
}

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && ( [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_BRANCH" == "development" ]); then
  echo -e "Starting to deploy to gh-pages\n"

  # cd into temporary deployment work directory
  cd deployment-work
  
  echo -e "Repository Content\n"
  ls
  cd development
  echo -e "\nDevelopment Directory\n"
  ls
  cd ..

  # add, commit and push files
#  git add -f -A
#  git commit -m "[ci skip] Deploy Travis build #$TRAVIS_BUILD_NUMBER to gh-pages"
#  git push -fq origin gh-pages > /dev/null 2>&1 || error_exit "Error uploading the build result to gh-pages"

  # go back to the directory where we started
  cd ..

  echo -e "Done with deployment to gh-pages\n"
fi