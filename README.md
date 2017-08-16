ld Instructions -

1. Java 1.7 or higher and Tomcat 7.0 or above needs to be installed.
2. MongoDb is used as persistence system which needs to be installed if you may need to test the code on your machine. Environment is also setup on the AWS EC2 instance , so use this url (http://34.209.101.121:8080/urlShortner) to be able to test the functionality of the solution. To test specifics on the code with different test cases you need to install MongoDb locally. 
3. This project is to imported as a Maven project.
4. Eclipse or similar IDE is recommended which can support Tomcat. 
5. Build Maven and deploy on Tomcat using Eclipse/IDE.

Solution -

Functionalities -
1. Given a long URL it converts into a short URL using the Hashing function clicking the respective button.
2. Given a short URL it returns the long URL using Database/Cache clicking the respective button.
3. Clicking the short URL on the UI in the below list user is redirected to the original URL.
4. User will be able to blacklist a particular URL. If the blacklisted URL is hit it would lead to the main UI page again.


Components -

Hashing Function /URL encoding- base62 ([A-Z, a-z, 0-9])  to qualify the characters for the shortened version. 6 bits could represent 62^6=57 billion unique values for the cache. This method would provide a large base of distinct values. 

Servlets - One gets the user to the main UI of this service. Second is for redirection to original URL when clicked on the shortened URL. All services are build using Jersey.

Database (NoSql persistence) - Data is stored as ID (which is the hash of the document),  Short URL and Long URL in the database where we calculate the hash of the URL if that exists in the database then we return the value, else we store the value in the database. When there are redirect requests it checks the Cache first , if not present in it then it will fetch from the database. Since ID is also stored based on the hash value of the document


Caching (LRU) - As the number of reads are way more then write in real scenario, so whenever there is read / redirect request it check the Cache first and if not present would again check the database to get the original URL.

Workflow - 

Every functionality work in O(1) in most cases unless there is collision. User demands to shortened a URL for the first time, the Hash function is run and shortened URL is delivered. The data is stored in the database. Thus when user requires the original version of the shortened version we check the value if not present in cache looks up the database which is also constant time to get the original URL. Cache would make it even faster. 

Design -  

- JSON everywhere
- Singleton Pattern for Cache and Encoding Base62 algorithm instances
- Stateless 

Traffic and Storage Estimation -

This system would have a lot of redirect requests compared to the number of new Shortenings. We assume 50:1 to 100:1 of read to write.

If we assume that we would have 500M new URLs shortenings per month, we can expect (50 * 500M => 25B to 50B) redirections during the same time. What would be Queries Per Second (QPS) for our system?
New URLs shortenings per second:
~= 100 URLs/s to 200 URLs/s
URLs redirections per second:  ~= 15K/s to 20 K/s
															
	Storage Estimation - 

	We can expect 400 - 500 M requests and we would prefer to keep the URL for 4-5 years than the 		storage requirement comes out to be - 

	500 million * 5 years * 12 months = 30 billion

	10-15 TB of storage.

	Cache Estimation - 

	We can follow the 20 - 80 rule and reserve 20 % of the traffic as the cache. Since we have 19K 		requests per second, we would be getting 1.7billion requests per day.

	To cache 20% of these requests, we would need 170GB of memory.

	0.2 * 1.7 billion * 500 bytes ~= 170GB

Database Design -

	Requirements for the Database- 
	
	The service is read heavy.
	We need to store billions of objects 
	Each object should be as small as possible.

	SQL vs NoSQL?
	There are no relationships necessary between the object and the storage is heavy which makes a 		No rich query are needed and QPS requirement is high. SQL would also require developers to write 	code to scale the service. This would be a waste of resources.
	NoSql database more preferable.
	However, most of the web frameworks support SQL better.

Hashing Function -

	MD5 - 128 bit , SHA - 160 bit and SHA - 256 bit can be used to create unique hash extract the 		shortened URL. 
	- We could take the first 6 chars of the converted string.
	- This will cause conflicts after 1 billion requests 
	Thus we can use we can use - 

	base62 ([A-Z, a-z, 0-9])  to qualify the characters for the shortened version. 6 bits could represent 	62^6=57 billion. 

	Work Arounds - 
		To resolve collision use (long_url + timestamp) as the hash function key. 2. When conflicts, 			regenerates the hash value(it's different because timestamp changes).
	
		We can also increase from 62 to 64 bits by adding ‘.’ and ‘-’ in the allowed characters. We can 		also increase the number of characters in the shortened URL to 7 or 8 to handle more cases 			without collision.

		Each URL can be appended with an increasing sequence number which is not be stored in the 		database. We can generate the hash of this function. Again we need to think how big would be 		the number and what happens when it overflows.

Offline Key Generation- 

	A standalone service can be implemented which generates the keys of 6-7 chars of shortened URL 	without having the received the requests. When we have a request for that we would simply provide a 	URL from the generated values. This would make the service even faster and we do not have to 		encode the URL and the make sure there are no collisions in those values.

	Issues with this approach -

	- This can be a point of failure so we may need a replica of a different server.
	
Database Sharding and Replication -

We need to store billions of the URL in the database thus we may need to partition with different schemes.

	Range Based -
		We can partition the storage based on the first character of the hash string in the alphabetical 		order. The problem with this approach would be that it can lead to unbalanced partitions. But 			we can work the partitions according to the frequencies of the characters as a work around.

	Hash Based -
		Our hashing function will randomly distribute URL into different partitions, e.g., our hashing 			function can always map any key to a number between [1…256], and this number would 				represent the partition to store our object. 

Sharding can be of two types - Horizontal Sharding and Vertical Sharding. Horizontal would be a good approach.
We can also use a separate machine to maintain the field with which sharding is implemented or we can also use Zookeeper.


Caching 

We can integrate caching service like Memcached or Redis to implement caching policies for our service. Ideally starting with 20% the daily traffic of the website.

Eviction Policy is chosen as the Least Recently used URL as the number of the reads to writes are high which suits our choice.

Load Balancing 

Load balancing is performed to distribute the requests equally on the servers and if one of the servers is down. Load balancing can be implemented between Clients and Application servers , and Application servers and Database Servers.

Messaging Queue 

If the number of concurrent requests increase more 5000 per second we might require a messaging queue service like RabbitMQ to handle those requests so none of the requests are lost.

Database Cleaning 

We need to decide if the entry in the database is persisting forever or we may want to flush it after some period of time (default expiration time can be 3 years). We can attempt a lazy cleanup for the expired links such that there is not much pressure on the database. After removing an expired link, we can put the key back in the key-DB to be reused.

Issues or cases which the solution have not covered with our solution - 

- If multiple users enter the same URL then all of them should get the same URL or different URL ?
- What if the two URL are same except the encoding ?
- Blacklist doesn’t work if URL is present in cache.
