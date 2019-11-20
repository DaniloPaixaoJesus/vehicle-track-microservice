package br.com.danilopaixao.vehicle.test;
import com.lambdaworks.redis.*;

public class RedisLabTest {
	
	public static void main(String[] args) {
	    
		//redis://aH9WHGiUkpBOoZHF46kciNl1p6eNirEW@redis-10508.c9.us-east-1-4.ec2.cloud.redislabs.com:10508
		RedisClient redisClient = new RedisClient(
	    RedisURI.create("redis://kN2CUQ0lVUmyPbNG9PbprtiOnvILRh9n@localhost:6379"));
	    RedisConnection<String, String> connection = redisClient.connect();

	    System.out.println("Connected to Redis");
	    
	    connection.set("foo", "bar");
	    String value = connection.get("foo");
	    System.out.println(value);

	    connection.close();
	    redisClient.shutdown();
	  }

}
