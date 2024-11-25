package sia.finance_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import sia.finance_tracker.entity.Category;
import sia.finance_tracker.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create a new Category.css
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Get all Categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get Category.css by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category.css not found"));
    }

    // Update Category.css by ID
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category.css not found"));

        existingCategory.setName(updatedCategory.getName());
        return categoryRepository.save(existingCategory);
    }

    // Delete Category.css by ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

