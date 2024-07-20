package com.reactive.web.controller;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactive.web.model.Tweet;

import reactor.core.publisher.Flux;

@RestController
public class TweetsController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass()); 

	//check the properties. Max treads were limited to 2 threads for better testing/understanding
	@GetMapping("/slow-service-tweets")
	private List<Tweet> getAllTweets() {
		
	    try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // delay
	    
	    return Arrays.asList(
	      new Tweet("RestTemplate rules", "@user1"),
	      new Tweet("WebClient is better", "@user2"),
	      new Tweet("OK, both are useful", "@user1"));
	}
	
	
	@GetMapping("/tweets-blocking")
	public List<Tweet> getTweetsBlocking() {
	    log.info("Starting BLOCKING Controller!");
	    final String uri = "http://localhost:8080/slow-service-tweets";

	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<List<Tweet>> response = restTemplate.exchange(
	      uri, HttpMethod.GET, null,
	      new ParameterizedTypeReference<List<Tweet>>(){});

	    List<Tweet> result = response.getBody();
	    result.forEach(tweet -> log.info(tweet.toString()));
	    log.info("Exiting BLOCKING Controller!");
	    return result;
	}
	
	@GetMapping(value = "/tweets-non-blocking", 
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Tweet> getTweetsNonBlocking() {
	    log.info("Starting NON-BLOCKING Controller!");
	    Flux<Tweet> tweetFlux = WebClient.create()
	      .get()
	      .uri("http://localhost:8080/slow-service-tweets")
	      .retrieve()
	      .bodyToFlux(Tweet.class);
	
	    tweetFlux.subscribe(tweet -> log.info(tweet.toString()));
	    log.info("Exiting NON-BLOCKING Controller!");
	    return tweetFlux;
	}
}
