# twitter getter 

Set your search terms in an environment variable called _SEARCH_TERMS_ and pass in the required parameters.

__Examples__:

Set search terms:
```
export SEARCH_TERMS='marijuana,hashish,cannabinoids,cannabis,weed,bong,medicalmarijuana,LegalizeMarijuana,marijuanaprohibition,mmj,mmot,daggadebate,breakthetaboo,legalizecannabis,cannabisismedicine,sensi seed,sensi seeds,kush seed,kush seeds, mmar,mmpr,SpringDuby16'
```

Run collector
```
sbt "run -awsAccessKey your-awsaccesskey  -consumerKey your-consumerkey -consumerSecret your-consumersecret -accessToken your-accesstoken -accessTokenSecret your-accesstokensecret"
```

note: This is pretty much entirely based on the [twitter classifier reference app](https://github.com/databricks/reference-apps)

![alt text](https://dl.dropboxusercontent.com/s/zsgtkc55k68vp88/baby_baluga.png "Baby Baluga") 
