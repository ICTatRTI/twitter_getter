#!/usr/bin/env bash

export JAVA_OPTS='-XX:MaxPermSize=256M -Xmx512M'

scala ./target/scala-2.11/twitter_getter-assembly-1.0.jar  -consumerKey YOUR_KEY -consumerSecret YOUR_SECRET -accessToken YOUR_TOKEN -accessTokenSecret YOUR_SECRET_TOKEN -awsAccessKey YOUR_AWS_KEY -awsSecretAccessKey YOUR_AWS_SECRET_KEY -awsBucketName YOUR_BUCKET_NAME