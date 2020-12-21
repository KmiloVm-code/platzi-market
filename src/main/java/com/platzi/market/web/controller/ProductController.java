package com.platzi.market.web.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/all")
	@ApiOperation(value = "Get all supermarket products", authorizations = {@Authorization(value = "JWT")})
	@ApiResponse(code = 200, message = "OK")
	public ResponseEntity<List<Product>> getAll(){
		return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	@ApiOperation(value = "Search a product whit an ID", authorizations = {@Authorization(value = "JWT")})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Product not found")
	})
	public ResponseEntity<Product> getProduct(@ApiParam(value = "The id of product", required = true, example = "7") @PathVariable("productId") int productId){
		return productService.getProduct(productId)
				.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/category/{categoryId}")
	@ApiOperation(value = "Get products by category", authorizations = {@Authorization(value = "JWT")})
	public ResponseEntity<List<Product>> getByCategory(@PathVariable("categoryId") int categoryId){
		return productService.getByCategory(categoryId)
				.map(products -> new ResponseEntity<>(products, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/save")
	@ApiOperation(value = "Save products", authorizations = {@Authorization(value = "JWT")})
	public ResponseEntity<Product> save(@RequestBody Product product) {
		return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{productId}")
	@ApiOperation(value = "Delete product", authorizations = {@Authorization(value = "JWT")})
	public ResponseEntity delete(@PathVariable("productId") int productId) {
		if (productService.delete(productId)){
			return new ResponseEntity(HttpStatus.OK);
		}else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
}
