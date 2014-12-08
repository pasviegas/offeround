
##Solution 1

For this solution I tried to stick with the existing stack. Optimized the tables for quering, the updates for minimum indexing and using elasticsearch only for fulltext search (product name).

- As pointed, one product can have many offers (from different retailers), so offers could be only inserted and updated and never deleted. In such case every store would always have 1 offer for every product. The offer model could even be preheated (if enough hard disk) so reindexing would drop significantly.
	
```javascript
//Offer
{
 "_id":ObjectId("5485f456bb91066ac5154d4c"),
 "product":{  
    "_id":ObjectId("5485f456bb91066ac5154d4c"),
    ...
 },
 "store":{  
    "_id":ObjectId("5485f456bb91066ac5154d4c"),
    ....
    }
 },
 ...
 "price":12.3,
 "quantity":1
}
```
In this way one could use upserts instead of inserts to reduce the amount of indexes to be calculated (using $setOnInsert):

```javascript
//Given:
db.offers.ensureIndex( { "product.name": 1 } )
db.offers.ensureIndex( { "product.name": 1, "store.name": 1 } )

var offerUpdate = ....

db.offers.update(
   {//Query
     "product.name": offerUpdate.product.name,
     "store.name": offerUpdate.store.name
   },
   {//Indexes are only modified on on insert
     "$setOnInsert": {
     	"product": {
     	  "name": offerUpdate.product.name,
     	  ...
     	},
     	"store": {
     	  "name": offerUpdate.store.name,
     	  ...
     	}
     },
     //Quantity and price are always updated
     "$set": {
     	"price": offer.price,
     	"quantity": offer.quantity,
     	...
     }
   },
   {
     upsert: true
   }
)
```
- Join documents as needed for queries. The offer model would have all the data needed for queries by product, location and category.

```javascript
//Offer
{
 "_id":ObjectId("5485f456bb91066ac5154d4c"),
 "product":{  
    "id":ObjectId("5485f456bb91066ac5154d4c"),
    "name":"Stetson Woodfield"
 },
 "store":{  
    "id":ObjectId("5485f456bb91066ac5154d4c"),
    "name":"Store 1 - CAPS AND SOX BERLIN",
    "location":{  
       "type":"Point",
       "coordinates":[  
          13.41,
          52.51
       ]
    }
 },
 "categories":[  
    "Sport",
    "Houseware"
 ],
 "price":12.3,
 "quantity":1
}

//The index and the upsert would have to change to account category and location 
db.offers.ensureIndex( { "store.location": "2dsphere" } )

var offerUpdate = ....

db.offers.update(
   {//Query
     "product.name": offerUpdate.product.name,
     "store.name": offerUpdate.store.name
   },
   {//Indexes are only modified on on insert
     "$setOnInsert": {
     	"product": {
     	  "name": offerUpdate.product.name,
     	  ...
     	},
     	"store": {
     	  "name": offerUpdate.store.name,
     	  "location": ....
     	},
     	"categories": offerUpdate.categories
     },
     //Quantity and price are always updated
     "$set": {
     	"price": offer.price,
     	"quantity": offer.quantity,
     	...
     }
   },
   {
     upsert: true
   }
)

```

- Elasticsearch would only store product names and be used for fulltext queries, other queries would be made directly in mongo. Elasticsearch indexes would also never be updated only inserted as they only hold product name information:

```scala

// Elasticsearch config (http://www.elasticsearch.org/guide/en/elasticsearch/hadoop/current/configuration.html): 
//
//  es.write.operation = "upsert"
//  es.mapping.id = "name" -- product name is the unique identifier, could be also prod
//
//  {"_index":"offeround","_type":"products","_id": "Stetson Woodfield","_score":1.0,"_source":{"name":"Stetson Woodfield"}}
//

// Queries using only one document, no joins
//
// - /search/product/:name
// Hits elasticsearch and retrieves the names. (can be used in the search box)
def searchProductsByName(name: String) = Action.async {
  esProductsByName(name).map {
    products => Ok(Json.toJson(products))
  }
}
 
// - /search/category/:name 
// Hits mongo and retrieve the offers with the matching category. (search by category)
def closestOffersByCategoryName(name: String) = Action.async {
  closestOffersWith(
    BSONDocument("categories" ->
      BSONDocument("$in" -> BSONArray(name))
    )
  )
}
 
// - /search/product/offers/:name
// Gets all the offers for the exact product name. (could be used in product detail)
def offersByExactProductName(name: String) = Action.async {
  offersWith(obj("product.name" -> name))
}
 
// - /search/product/closest/:name
// Hits the elasticsearch (could be cached before) and retrieves from mongo the closest offers matching the list of names
// (offer list, after home search)
def closestOffersSearchingByProductName(name: String) = Action.async {
  esProductsByName(name).map {
    products => products.map(_.name)
  }.flatMap {
    names => {
      closestOffersWith(BSONDocument("product.name" -> BSONDocument("$in" -> names)))
    }
  }
}

//executes the aggregate query and returns as json response
def closestOffersWith(query: BSONDocument) = {
  db.command(RawCommand(closestWith(query))).map(results => {
    results.getAsTry[BSONArray]("results") match {
      case Success(list) => Ok(toJson(list))
      case Failure(_) => NotFound
    }
  })
}

//create an aggregate query to find the closest given the location
//right now is defaulting to Berlin
//
//db.runCommand({
//   geoNear: "offers",
//   near: { type: "Point" , coordinates: [ ... ] } ,
//   spherical: true,
//   query: ...,
//})
def closestWith(query: BSONDocument): BSONDocument = {
  BSONDocument(
    "geoNear" -> "offers",
    "near" -> BSONDocument("type" -> "Point", "coordinates" -> BSONArray(52.5167, 13.3833)),
    //"maxDistance" -> 10000,
    "spherical" -> true,
    "query" -> query
  )
}

//retrieve names from elasticsearch
//for the purpose of this example I am using a default search query
//this call could be cached for better performance
private def esProductsByName(name: String): Future[Array[Product]] = {
  import scala.collection.JavaConversions._
  esClient.execute {
    search in "offeround/products" query name
  }.map {
    searchResponse => {
      searchResponse.getHits.hits.map(hit => {
        val map = hit.getSource.toMap
        Product(map("name").asInstanceOf[String])
      })
    }
  }
}
```

- For importing the data it is possible to parallelize using Sparks mapreduce for optimum performance (or streamed given a time frame)
```scala
//Given
case class Product(name: String) 
case class Store(name: String, longitude: Double, latitude: Double)
case class Offer(price: Double,
                 quantity: Int,
                 product: Product,
                 store: Store,
                 categories: List[Category])

//This is a Spark job
object Retailer12345Job {

  def main(args: Array[String]) {
    //Could be a realtime stream
    //val sparkContext = new StreamingContext(conf, Seconds(10)
  
    //10 is the amount of threads assigned
    val sparkContext = new SparkContext("local[10]", "Offeround")

    //Read data from source
    val retailerXml = sparkContext.wholeTextFiles("Retailer12345.xml")

    //Normalize data
    val offers = retailerXml.flatMap(t => toOfferList(t._2))

    //Save all offers to mongo
    // toBSON creates an upsert as described above, so we index only when inserting 
    // and always update price and quantity
    offers.map(_.toBSON()).saveToMongo("mongodb://127.0.0.1:27017/offeround.offers")

    //Save only the product name to es
    val esSettings = Map("es.write.operation" -> "upsert", "es.mapping.id" -> "name")
    offers.map(o => o.product.toES()).saveToEs("offeround/products", esSettings)

    //Stop context
    sparkContext.stop()
  }
}
```

##Solution 2

This solution requires a big change in the existing stack, but given the existing models seems to be a good fit.

	constraint: it would not handle typos properly, but I don't know if it is extremely necessary. 

- I would change the database to a SQL db. 
####Reasons:
    - Most of your data is already normalised. Apart from field retailer in Offers table, everything else is normalised.
    - The queries are mostly joined which would have a good improvement in a SQL environment. 
    - Inserting is also a very cheap operation o SQL databases.

- I would create a table for storing “like” indexes, which would give you linear performance on search. In this table I would store a hash of every character, separated by words, of the product name and its corresponding product id, if a joined query is needed (product name + store) would be simple to make a modification to the hash to account the other field. This would not be the primary index but an indexed field. Sql databases are very good at optimizing storage and hard disk doesn't seem to be a constraint, so I don't think this would be an issue.

####example:
After inserting product with name “mac”

| id	        | search_hash   | product_id  |
| ------------- |:--------------| -----------:|
| 1 | hash(m) | 1 |
| 2 | hash(ma) | 1 |
| 3 | hash(mac) | 1 |

Then after inserting product with name “microsoft”


| id	        | search_hash   | product_id  |
| ------------- |:--------------| -----------:|
| 1 | hash(m) | 1 |
| 2 | hash(ma) | 1 |
| 3 | hash(mac) | 1 |
| 4 | hash(m) | 2 |
| 5 | hash(mi) | 1 |
| 6 | hash(mic) | 1 |
| 7 | hash(micr) | 1 |
| 8 | hash(micro) | 1 |
| 9 | hash(micros) | 1 |
| 10 | hash(microso) | 1 |
| 11 | hash(microsof) | 1 |
| 12 | hash(microsoft) | 1 |
	
This way, quering would be very straight forward:
```sql
SELECT PRODUCT_ID FROM SEARCH_INDEX_PRODUCT WHERE SEARCH_HASH = hash(m)
```		
	returns product_id 1 and 2
	

- For importing, even if SQL db is not an option I would consider using Spark for parallel execution of the jobs, it has connectors for Elasticsearch, Mongo and JDBC and it can be easily clustered, has a killing performance. 
