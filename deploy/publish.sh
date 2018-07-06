#!/bin/bash

if [ "${TRAVIS_JDK_VERSION}" == "oraclejdk8" ] ; then
  if [ "${TRAVIS_PULL_REQUEST}" == "false" ] ; then
    if [ "${TRAVIS_BRANCH}" == "master" ] ; then
      mvn deploy --settings $GPG_DIR/settings.xml -DperformRelease=true -DskipTests=true
      exit $?
    else
      echo "Deploy skipped: Only the master branch"
    fi
  else
    echo "Deploy skipped: On pull request"
  fi
else
  echo "Deploy skipped: Only on Oracle oraclejdk8"
fi