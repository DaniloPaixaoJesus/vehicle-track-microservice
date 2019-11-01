package br.com.danilopaixao.vehicle.track;
import com.lambdaworks.redis.*;

public class RedisLabTest {
	
	public static void main(String[] args) {
	    
		//redis://kN2CUQ0lVUmyPbNG9PbprtiOnvILRh9n@redis-19635.c52.us-east-1-4.ec2.cloud.redislabs.com:19635
		RedisClient redisClient = new RedisClient(
	    RedisURI.create("redis://kN2CUQ0lVUmyPbNG9PbprtiOnvILRh9n@redis-19635.c52.us-east-1-4.ec2.cloud.redislabs.com:19635"));
	    RedisConnection<String, String> connection = redisClient.connect();

	    System.out.println("Connected to Redis");
	    
	    connection.set("foo", "bar");
	    String value = connection.get("foo");
	    System.out.println(value);

	    connection.close();
	    redisClient.shutdown();
	  }

}
