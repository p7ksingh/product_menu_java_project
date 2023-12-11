package com.viral.menu;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	//New 17-11 16:05
	
	   @Query("SELECT p.name FROM Product p")
	    List<String> findAllProductNames();
	   Product findByName(String name);
	
};