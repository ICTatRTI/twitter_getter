# twitter getter 

Set your search terms in an environment variable called _SEARCH_TERMS_ and pass in the required parameters.

__Examples__:

Set search terms:
```
export TWITTER_SEARCH_TERMS='term1,term2,term3'
```

Run collector
```
sbt "run -awsAccessKey your-awsaccesskey  \
-consumerKey your-consumerkey \
-consumerSecret your-consumersecret \
-accessToken your-accesstoken \
-accessTokenSecret your-accesstokensecret"
```

Package and run
```
sbt clean assembly
nohup scala ./target/scala-2.11/twitter_getter-assembly-1.0.jar &
```

Baby Baluga says "This is pretty much entirely based on the [twitter classifier reference app](https://github.com/databricks/reference-apps)"s

![alt text](https://dl.dropboxusercontent.com/s/zsgtkc55k68vp88/baby_baluga.png "Baby beluga in the deep blue sea, Swim so wild and you swim so free. Heaven above and the sea below, And a little white whale on the go.") 
