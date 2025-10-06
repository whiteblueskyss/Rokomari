


#### 2. **Basic Redis Commands**

Let’s walk through some of the most commonly used commands in Redis:

* **SET**: This command sets a key with a value.

  ```bash
  SET mykey "Hello, Redis!"
  ```

  This command will store the string `"Hello, Redis!"` with the key `mykey`.

* **GET**: This retrieves the value stored with a key.

  ```bash
  GET mykey
  ```

  This will return the value associated with `mykey`, which is `"Hello, Redis!"`.

* **DEL**: This deletes a key from Redis.

  ```bash
  DEL mykey
  ```

  After running this command, the `mykey` will no longer exist.

* **EXPIRE**: You can set an expiration time for a key.

  ```bash
  EXPIRE mykey 60
  ```

  This command will make `mykey` expire after 60 seconds.

* **KEYS**: You can list all the keys stored in Redis.

  ```bash
  KEYS *
  ```

  This command will return a list of all keys in the database.

#### 3. **Redis Data Types**

Redis supports several data structures, and here are a few of the basic types:

* **Strings**: The simplest type in Redis.

  * **SET**: Store a string value.
  * **GET**: Retrieve the string value.

* **Lists**: A simple list of values.

  * **LPUSH**: Add an element to the left of the list.
  * **RPUSH**: Add an element to the right of the list.
  * **LRANGE**: Get elements from a list.

  ```bash
  LPUSH mylist "item1"
  RPUSH mylist "item2"
  LRANGE mylist 0 -1
  ```

* **Hashes**: Store a collection of field-value pairs.

  ```bash
  HSET myhash field1 "value1"
  HGET myhash field1
  HDEL myhash field1
  ```

* **Sets**: A collection of unique values.

  ```bash
  SADD myset "value1"
  SADD myset "value2"
  SMEMBERS myset
  ```

* **Sorted Sets**: Like Sets, but each element is associated with a score.

  ```bash
  ZADD myzset 1 "value1"
  ZADD myzset 2 "value2"
  ZRANGE myzset 0 -1
  ```

#### 4. **Working with Redis in Java (Jedis)**

To get started with Redis in your Java Spring Boot project, you can use the Jedis library, which is a popular Java client for Redis.

* **Add Jedis to your `pom.xml`**:

  ```xml
  <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>3.6.1</version>
  </dependency>
  ```

* **Connecting to Redis in Java**:
  Here’s a simple Java code example for interacting with Redis:

  ```java
  import redis.clients.jedis.Jedis;

  public class RedisExample {
      public static void main(String[] args) {
          Jedis jedis = new Jedis("localhost"); // Connect to Redis
          jedis.set("mykey", "Hello, Redis!");
          System.out.println("Stored value in Redis: " + jedis.get("mykey"));
          jedis.close(); // Close the connection
      }
  }
  ```
