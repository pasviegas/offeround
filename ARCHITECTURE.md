1. I would change the database to a SQL db. 
####Reasons:
    - Most of your data is already normalised. Apart from field retailer in Offers table, everything else is normalised.
    - The queries are mostly joined which would have a good improvement in a SQL environment. 
    - Inserting is also a very cheap operation o SQL databases.

2. I would create a table for storing “like” indexes, which would give you linear performance on search. In this table I would store a hash of every character, separated by words, of the product name and its corresponding product id, if a joined query is needed (product name + store) would be simple to make a modification to the hash to account the other field. This would not be the primary index but an indexed field. Sql databases are very good at optimizing storage and hard disk doesn't seem to be a constraint, so I don't think this would be an issue.

####example:
	after inserting product with name “mac”

| id	        | search_hash   | product_id  |
| ------------- |:--------------| -----------:|
| 1 | hash(m) | 1 |
| 2 | hash(ma) | 1 |
| 3 | hash(mac) | 1 |

    then after inserting product with name “microsoft”


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
###### SELECT PRODUCT_ID FROM SEARCH_INDEX_PRODUCT WHERE SEARCH_HASH = hash(m)
		
	returns product_id 1 and 2
	

3. For importing, even if SQL db is not an option I would consider using Spark for parallel execution of the jobs, it has connectors for Elasticsearch, Mongo and JDBC and it can be easily clustered, has a killing performance. 
