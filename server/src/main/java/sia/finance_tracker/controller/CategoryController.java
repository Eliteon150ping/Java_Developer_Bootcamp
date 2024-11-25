package sia.finance_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.finance_tracker.entity.Category;
import sia.finance_tracker.service.CategoryService;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Categories", description = "Categories for transactions.")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create a new Category.css
    @Operation(
            summary = "Create a Category.css.",
            description = "Create a Category.css object. The response is Category.css object with its id and name.",
            tags = { "categories", "post" })
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Get all Categories
    @Operation(
            summary = "Retrieve all Categories.",
            description = "Get all Category.css objects. The response is all Category.css objects with its id and name.",
            tags = { "categories", "get" })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get Category by ID
    @Operation(
            summary = "Retrieve a Category by Id.",
            description = "Get a Category object by its Id. The response is Category.css object with its id and name.",
            tags = { "categories", "get" })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Update an existing Category by ID
    @Operation(
            summary = "Update a Category.css by Id.",
            description = "Update a Category object. The response is the category's object with its name being updated.",
            tags = { "categories", "put" })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category by ID
    @Operation(
            summary = "Delete a Category.css by Id.",
            description = "Delete a Category.css object. The response is the category's object with its name being deleted.",
            tags = { "categories", "delete" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
