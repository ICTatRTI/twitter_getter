# twitter getter

Set your search terms in an environment variable called `SEARCH_TERMS' and pass in the required parameters.

Examples:

Set search terms:
```
export SEARCH_TERMS='marijuana,hashish,cannabinoids,cannabis,weed,bong,medicalmarijuana,LegalizeMarijuana,marijuanaprohibition,mmj,mmot,daggadebate,breakthetaboo,legalizecannabis,cannabisismedicine,sensi seed,sensi seeds,kush seed,kush seeds, mmar,mmpr,SpringDuby16'
```

Run collector
```
sbt "run -awsAccessKey your-awsaccesskey  -consumerKey your-consumerkey -consumerSecret your-consumersecret -accessToken your-accesstoken -accessTokenSecret your-accesstokensecret"
```

note: This is pretty much entirely based on the [twitter classifier reference app](https://github.com/databricks/reference-apps)