package com.viral.menu;


import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface ProductRest {
	
	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);
	ResponseEntity<String> updateMenuItem( Map<String, String> requestMap);

	ResponseEntity<String> deleteMenuItem(Integer productId);
	
	
	
	
	
}