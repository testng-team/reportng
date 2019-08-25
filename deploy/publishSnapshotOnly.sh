#!/bin/bash

if [ "${TRAVIS_JDK_VERSION}" == "oraclejdk8" ] ; then
  if [ "${TRAVIS_PULL_REQUEST}" == "false" ] ; then
    if [ "${TRAVIS_BRANCH}" == "master" ] ; then
      ./gradlew publishSnapshotOnly
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