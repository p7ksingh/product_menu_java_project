package com.viral.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productrepository;
	
	  @Override
	    public List<Product> getAllMenuItems() {
	        // Implement the logic to retrieve all menu items (products) from the repository
	        return (List<Product>) productrepository.findAll();
	    }



	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
				
			
			if(validateProductMap(requestMap,false)) {
				productrepository.save(getProductFromMap(requestMap));
				//return RestaurantUtils.getResponseEntity(RestaurantConstants.Invalid_DATA, HttpStatus.BAD_REQUEST);
				return RestaurantUtils.getResponseEntity("Product added Successfully",HttpStatus.OK);
			}
			
		
			else {
				RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return RestaurantUtils.getResponseEntity(RestaurantConstants.Something_Went_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	
	@Override
	public ResponseEntity<String> updateMenuItem( Map<String, String> requestMap) {
	    try {
	        if (productrepository.existsById(Integer.parseInt(requestMap.get(("productId"))))) {
	            if (validateProductMap(requestMap, true)) {
	                productrepository.save(getProductFromMap(requestMap));
	                return RestaurantUtils.getResponseEntity("Menu item updated successfully", HttpStatus.OK);
	            } else {
	                return RestaurantUtils.getResponseEntity(RestaurantConstants.Invalid_DATA, HttpStatus.BAD_REQUEST);
	            }
	        } else {
	            return RestaurantUtils.getResponseEntity("Menu item not found", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return RestaurantUtils.getResponseEntity(RestaurantConstants.Something_Went_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteMenuItem(Integer productId) {
	    try {
	        if (productrepository.existsById(productId)) {
	            productrepository.deleteById(productId);
	            return RestaurantUtils.getResponseEntity("Menu item deleted successfully", HttpStatus.OK);
	        } else {
	            return RestaurantUtils.getResponseEntity("Menu item not found", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return RestaurantUtils.getResponseEntity(RestaurantConstants.Something_Went_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	


	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if(requestMap.containsKey("name")){
		  if(requestMap.containsKey("productId") && validateId) {
			  return true;
		  }
		  else if(!validateId) {
			  return true;
		  }
	}
		return false;

}
	/*private Product getProductFromMap(Map<String, String> requestMap) {
		Product product= new Product();
		
		product.setProductId(Integer.parseInt(requestMap.get("productId")));

		product.setStatus(Integer.parseInt(requestMap.get("status")));
	
		product.setName(requestMap.get("name"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
	}*/
	private Product getProductFromMap(Map<String, String> requestMap) {
	    Product product = new Product();

	    String productIdString = requestMap.get("productId");
	    if (productIdString != null && !productIdString.isEmpty()) {
	        try {
	            product.setProductId(Integer.parseInt(productIdString));
	        } catch (NumberFormatException e) {
	            
	            e.printStackTrace();
	     
	        }
	    }

	    product.setStatus(Integer.parseInt(requestMap.get("status")));
	    product.setName(requestMap.get("name"));
	    product.setPrice(Integer.parseInt(requestMap.get("price")));

		return product;
	}
	//New 17-11 16:07
	 @Override
	    public List<String> getAllProductNames() {
	        return productrepository.findAllProductNames();
	    }
}
