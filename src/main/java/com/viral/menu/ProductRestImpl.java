package com.viral.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/mymenu")	
public class ProductRestImpl implements ProductRest {
	
	@Autowired
	ProductService productservice;

    @Autowired
    ProductRepository productrepository;
	
	
	 @GetMapping
	    public String getMenuPage() {
	        return "menu";
	    }
	    @GetMapping("/list")
	    public String getMenuList(Model model) {
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);

	        System.out.println("Product List Size: " + productList.size());
	        return "menu";
	    }
	    //New 17-11 16:07
	    @GetMapping("/getProductNames")
	    @ResponseBody
	    public List<String> getProductNames() {
	        return productservice.getAllProductNames();
	    }
	
	  
	    @PostMapping("/addNewProduct")
	    public String addMenuItem(@RequestParam Map<String, String> requestMap, Model model) {
	        ResponseEntity<String> responseEntity = productservice.addNewProduct(requestMap);
	        model.addAttribute("message", responseEntity.getBody());
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);

	        return "menu";
	    }


	    @PostMapping("/update")
	   /* public String updateMenuItemCall(@RequestParam Map<String, String> requestMap, Model model) {
	    	ResponseEntity<String> responseEntity = updateMenuItem(requestMap);
	        model.addAttribute("message", responseEntity.getBody());
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);

	        return "menu";
	    }*/
	    public String updateMenuItemCall(@RequestParam Map<String, String> requestMap, Model model) {
	        
	        String productName = requestMap.get("productNameUpdate");

	        
	        Product existingProduct = productrepository.findByName(productName); // Use productRepository here

	        if (existingProduct != null) {
	          
	            existingProduct.setName(requestMap.get("nameUpdate")); 
	            existingProduct.setStatus(Integer.parseInt(requestMap.get("statusUpdate")));
	            existingProduct.setPrice(Integer.parseInt(requestMap.get("priceUpdate")));

	           
	            productrepository.save(existingProduct); 
	            model.addAttribute("message", "Menu item updated successfully");
	        } else {
	            model.addAttribute("message", "Product not found");
	        }
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);


	        return "menu";
	    }
	    



	    /*@PostMapping("/delete")
	    public String deleteMenuItem(@RequestParam Integer productId, Model model) {
	        ResponseEntity<String> responseEntity = productservice.deleteMenuItem(productId);
	        model.addAttribute("message", responseEntity.getBody());
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);

	        
	        return "menu";
	    }*/
	    @PostMapping("/delete")
	    public String deleteMenuItem(@RequestParam String productNameDelete, Model model) {
	        // Fetch the product from the database using the name
	        Product existingProduct = productrepository.findByName(productNameDelete); // Use productRepository here

	        if (existingProduct != null) {
	            // Get the product ID
	            Integer productId = existingProduct.getProductId();

	            // Delete the product from the database using the ID
	            productrepository.deleteById(productId); // Use productRepository here

	            model.addAttribute("message", "Menu item deleted successfully");
	        } else {
	            model.addAttribute("message", "Product not found");
	        }
	        List<Product> productList = productservice.getAllMenuItems();
	        model.addAttribute("products", productList);	

	        return "menu";
	    }
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

		try {
			return productservice.addNewProduct(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return RestaurantUtils.getResponseEntity(RestaurantConstants.Something_Went_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}



   @Override
   public ResponseEntity<String> deleteMenuItem(@PathVariable Integer productId) {
       return productservice.deleteMenuItem(productId);
   }
   @Override
   public ResponseEntity<String> updateMenuItem(Map<String, String> requestMap) {
	   return  productservice.updateMenuItem(requestMap);

   }
}
	