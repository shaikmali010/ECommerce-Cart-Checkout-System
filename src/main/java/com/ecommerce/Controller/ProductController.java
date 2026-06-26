package com.ecommerce.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.DTO.ProductRequestDto;
import com.ecommerce.DTO.ProductResponseDto;
import com.ecommerce.Service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Validated
@Tag(
        name = "Product APIs",
        description = "APIs for managing product operations"
)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(
            summary = "Create Product",
            description = "Creates a new product after validating the request and checking duplicate product names."
    )
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(requestDto));
    }

    @GetMapping("/{productId}")
    @Operation(
            summary = "Get Product By Id",
            description = "Retrieves product details using the product ID."
    )
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping
    @Operation(
            summary = "Get All Products",
            description = "Retrieves all products with pagination and sorting."
    )
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "productId") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                productService.getAllProducts(page, size, sortBy, sortDir));
    }

    @GetMapping("/name/{productName}")
    @Operation(
            summary = "Get Product By Name",
            description = "Retrieves product details using the product name."
    )
    public ResponseEntity<ProductResponseDto> getProductByName(
            @PathVariable String productName) {

        return ResponseEntity.ok(
                productService.getProductByName(productName));
    }

    @PutMapping("/{productId}")
    @Operation(
            summary = "Update Product",
            description = "Updates an existing product using the product ID."
    )
    public ResponseEntity<ProductResponseDto> updateProductById(

            @PathVariable Long productId,

            @Valid @RequestBody ProductRequestDto requestDto) {

        return ResponseEntity.ok(
                productService.updateProductById(productId, requestDto));
    }

    @DeleteMapping("/{productId}")
    @Operation(
            summary = "Delete Product",
            description = "Deletes a product using the product ID."
    )
    public ResponseEntity<String> deleteProductById(
            @PathVariable Long productId) {

        productService.deleteProductById(productId);

        return ResponseEntity.ok("Product deleted successfully.");
    }
}