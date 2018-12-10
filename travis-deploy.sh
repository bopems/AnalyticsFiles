#!/bin/bash
if [ $TRAVIS_BRANCH == "master" ]; then
  docker login -u $DOCKER_LOGIN -p $DOCKER_PASSWORD
  export IMAGE_NAME=$DOCKER_IMAGE_PROJECT/$DOCKER_IMAGE_NAME
  docker images
  docker build -t $IMAGE_NAME:$TRAVIS_COMMIT .
  docker tag $IMAGE_NAME:$TRAVIS_COMMIT $IMAGE_NAME:latest
  docker images
  docker push $IMAGE_NAME
else
  echo "exit"
fi
