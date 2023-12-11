Building a Restaurant Menu Management System with Spring Boot.

If you’re a Spring Boot learner looking to build a practical web application, you’re in the right place. In this step-by-step guide, we’ll walk through the process of creating a Restaurant Menu Management System using Spring Boot, Spring Data JPA, Thymeleaf, and MySQL. By the end of this tutorial, you’ll have a fully functional web application where you can add, update, and delete menu items.



1. Project Setup:
Before we dive into coding, let’s set up a new Spring Boot project using Spring Initializr. This online tool simplifies the process of bootstrapping a Spring Boot application.

Open your web browser and go to Spring Initializr.
Select the following project settings:
Project: Gradle or Maven (Choose your preference)
Language: Java
Spring Boot: Latest stable version
Group: com.menu (or your preferred group name)
Artifact: menu (or your preferred project name)
Dependencies:
Spring Web
Spring Data JPA
Thymeleaf
MySQL Driver
3. Click the “Generate” button to download the project ZIP file.

4. Extract the downloaded ZIP file to a location of your choice.

Now, you have a basic Spring Boot project ready to be developed into a Restaurant Menu Management System.
Once your project is created, add the following configuration to the application.properties file:

# application.properties
spring.datasource.platform=mysql
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:your_url
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8083
debug=true
This configuration sets up the MySQL database connection and specifies the server port.

2. Database Setup:

Now, let’s create the MySQL database table for storing menu items. Execute the following SQL script in your MySQL database:

CREATE TABLE IF NOT EXISTS product (
    product_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status INT NOT NULL
);
3. Entity Class (Product):

Now, let’s define the Product class, representing the menu items in our restaurant. This class will be mapped to a database table using JPA annotations.


@Entity
@Table("product")
public class Product implements Serializable {
 /**
 * 
 */
 private static final long serialVersionUID = 1L;
public Integer getProductId() {
 return productId;
 }
public void setProductId(Integer productId) {
 this.productId = productId;
 }
public String getName() {
 return name;
 }
public void setName(String name) {
 this.name = name;
 }
public Integer getPrice() {
 return price;
 }
public void setPrice(Integer price) {
 this.price = price;
 }
public Integer getStatus() {
 return status;
 }
public void setStatus(Integer status) {
 this.status = status;
 }
public static Long getSerialversion() {
 return serialVersion;
 }
public static final Long serialVersion = 123456L;
@Id
 @Column("product_id")
 private Integer productId;
 
 @Column("name")
 private String name;
 
 @Column("price")
 private Integer price;
 
 @Column("status")
 private Integer status;
}
In this class, @Entity marks it as a JPA entity, @Table(name = “product”) specifies the table name, and @Id indicates the primary key.

4. Repository Interface (ProductRepository):
The ProductRepository interface extends CrudRepository to provide basic CRUD operations and includes custom queries.

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{
 
 
 
 @Query("SELECT p.name FROM Product p")
 List<String> findAllProductNames();
 Product findByName(String name);
 
};
The @Repository annotation marks this interface as a Spring Data repository, and @Query allows the definition of custom queries using JPQL.

5. Service Layer (ProductService):
Next, let’s create the ProductService interface, which declares methods for handling business logic related to menu items.

public interface ProductService {
 List<Product> getAllMenuItems();
 ResponseEntity<String> addNewProduct(Map<String,String> requestMap);
 
 ResponseEntity<String> updateMenuItem( Map<String, String> requestMap);
ResponseEntity<String> deleteMenuItem(Integer productId);
 
 List<String> getAllProductNames();
 
 
}
This interface defines methods for retrieving menu items, adding new items, updating items, deleting items, and fetching all product names.

6. Service Implementation (ProductServiceImpl):
Now, implement the ProductService interface in the ProductServiceImpl class.

@Service
public class ProductServiceImpl implements ProductService {
 
 @Autowired
 ProductRepository productrepository;
 
 @Override
 public List<Product> getAllMenuItems() {
 
 return (List<Product>) productrepository.findAll();
 }
@Override
 public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
 try {
 
 
 if(validateProductMap(requestMap,false)) {
 productrepository.save(getProductFromMap(requestMap));
 
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
 
 @Override
 public List<String> getAllProductNames() {
 return productrepository.findAllProductNames();
 }
}
The @Service annotation marks this class as a Spring service, and @Autowired injects the ProductRepository into the service.

7. REST API Interface (ProductRest):
Now, let’s create the ProductRest interface, which declares methods for handling REST API endpoints related to menu items.

public interface ProductRest {
 
 ResponseEntity<String> addNewProduct(Map<String, String> requestMap);
 ResponseEntity<String> updateMenuItem( Map<String, String> requestMap);
ResponseEntity<String> deleteMenuItem(Integer productId);
 
}
This interface defines methods for adding a new product, updating a menu item, and deleting a menu item.

8. REST API Implementation (ProductRestImpl):
Implement the ProductRest interface in the ProductRestImpl class.

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
 
 public String updateMenuItemCall(@RequestParam Map<String, String> requestMap, Model model) {
 
 String productName = requestMap.get("productNameUpdate");
Product existingProduct = productrepository.findByName(productName);
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
@PostMapping("/delete")
 public String deleteMenuItem(@RequestParam String productNameDelete, Model model) {
 
 Product existingProduct = productrepository.findByName(productNameDelete);
if (existingProduct != null) {
 
 Integer productId = existingProduct.getProductId();
productrepository.deleteById(productId);
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
 return productservice.updateMenuItem(requestMap);
}
}
 
Here, @Controller marks the class as a Spring MVC controller, and @RequestMapping(“/mymenu”) specifies the base URL for this controller. @Autowired injects the ProductService and ProductRepository into the controller.

9. RestaurantConstants.java & RestaurantUtils.java

public class RestaurantConstants {
 public static final String Something_Went_WRONG = "Something went Wrong";
 public static final String UNAUTHORIZED_ACCESS = "You are not authorized";
 public static final String Invalid_DATA = "Invalid Data";
 }
public class RestaurantUtils {
 private RestaurantUtils() {
}
 public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpstatus){
 return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpstatus);
 }
 
 
 }
10. Thymeleaf Templates:
Now, let’s create Thymeleaf templates for the menu management web interface. Include forms for adding, updating, and deleting menu items and display the list of menu items in a table.


<!DOCTYPE html>
 <html lang="en" xmlns:th="http://www.thymeleaf.org">
 
 <head>
 <meta charset="UTF-8">
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <title>Restaurant Menu</title>
 <! - Add Bootstrap CDN links →
 <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
 integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
 </head>
 
 <body class="container mt-5">
 <div class="jumbotron text-center">
 <h1 class="display-4">Restaurant Menu</h1>
 <p class="lead">Welcome to our restaurant menu management system.</p>
 </div>
 
 <div class="row">
 <div class="col-md-6">
 <div class="card">
 <div class="card-header">
 <h3>Add Menu Item</h3>
 </div>
 <div class="card-body">
 <form action="/mymenu/addNewProduct" method="post">
 <div class="form-group">
 <label for="name">Name:</label>
 <input type="text" class="form-control" id="name" name="name" required>
 </div>
 
 <div class="form-group">
 <label for="status">Availability:</label>
 <input type="text" placeholder="0 for not available 1 for available" class="form-control" id="status" name="status" required >
 </div>
 
 <div class="form-group">
 <label for="price">Price:</label>
 <input type="number" class="form-control" id="price" name="price" required>
 </div>
 
 <button type="submit" class="btn btn-primary">Add Menu Item</button>
 </form>
 </div>
 </div>
 </div>
 
 <div class="col-md-6">
 <div class="card">
 <div class="card-header">
 <h3>Update or Delete Menu Item</h3>
 </div>
 <div class="card-body">
 <div class="btn-group" role="group" aria-label="Update or Delete">
 <button type="button" class="btn btn-secondary" data-toggle="collapse" href="#updateForm" role="button" aria-expanded="false" aria-controls="updateForm">
 Update
 </button>
 <button type="button" class="btn btn-secondary" data-toggle="collapse" href="#deleteForm" role="button" aria-expanded="false" aria-controls="deleteForm">
 Delete
 </button>
 </div>
 
 <div class="collapse mt-3" id="updateForm">
 <form action="/mymenu/update" method="post">
 <div class="form-group">
 <label for="productNameUpdate">Product Name:</label>
 <select id="productNameUpdate" name="productNameUpdate" class="form-control" required>
 <option th:each="product : ${products}" th:value="${product.name}" th:text="${product.name}"></option>
 </select>
 </div>
 
 <div class="form-group">
 <label for="nameUpdate">Name:</label>
 <input type="text" class="form-control" id="nameUpdate" name="nameUpdate">
 </div>
 
 <div class="form-group">
 <label for="statusUpdate">Availability:</label>
 <input type="text" placeholder="0 for not available 1 for available" class="form-control" id="statusUpdate" name="statusUpdate">
 </div>
 
 <div class="form-group">
 <label for="priceUpdate">New Price:</label>
 <input type="number" class="form-control" id="priceUpdate" name="priceUpdate">
 </div>
 
 <button type="submit" class="btn btn-primary">Update Menu Item</button>
 </form>
 </div>
 
 <div class="collapse mt-3" id="deleteForm">
 <form action="/mymenu/delete" method="post">
 <div class="form-group">
 <label for="productNameDelete">Product Name:</label>
 <select id="productNameDelete" name="productNameDelete" class="form-control" required>
 <option th:each="product : ${products}" th:value="${product.name}" th:text="${product.name}"></option>
 </select>
 </div>
 
 <button type="submit" class="btn btn-danger">Delete Menu Item</button>
 </form>
 </div>
 </div>
 </div>
 </div>
 </div>
 
 <div class="mt-5">
 <h3>Product List</h3>
 <table class="table">
 <thead>
 <tr>
 <th>Name</th>
 <th>Status</th>
 <th>Price</th>
 <th>Product ID</th>
 </tr>
 </thead>
 <tbody>
 <tr th:each="product : ${products}">
 <td th:text="${product.name}"></td>
 <td th:text="${product.status}"></td>
 <td th:text="${product.price}"></td>
 <td th:text="${product.productId}"></td>
 </tr>
 </tbody>
 </table>
 </div>
 
 <! - Add Bootstrap JS and Popper.js CDN links →
 <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
 <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"></script>
 <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
 </body>
 </html>
These templates should include HTML code with Thymeleaf attributes to bind data from the backend. I have provided the HTML code I used you can change it according to your needs.

11. Output


Here you will be able to add, update, and delete items from the menu.

12. Running and Testing the Application:
Once you have finished coding, run your Spring Boot application and access the web interface to perform CRUD operations on menu items. You can test your RESTful API endpoints using tools like Postman or by using a local host on your browser.

13. Conclusion:
Congratulations! You’ve successfully built a Restaurant Menu Management System using Spring Boot. This project demonstrates the application of Spring Boot concepts in a practical scenario. Feel free to explore and expand upon this project as you continue your Spring Boot learning journey.



I appreciate your dedication to learning Spring Boot, and I’m excited to assist you on your coding journey. Happy coding!

Spring Boot
Spring
Spring Framework
