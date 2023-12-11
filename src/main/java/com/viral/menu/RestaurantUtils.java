package com.viral.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestaurantUtils {
	private RestaurantUtils() {

	}
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpstatus){
		return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpstatus);
	}
	
	
}