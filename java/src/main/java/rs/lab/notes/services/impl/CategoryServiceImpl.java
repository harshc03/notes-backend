package rs.lab.notes.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.lab.notes.data.model.Category;
import rs.lab.notes.exceptions.ObjectExistException;
import rs.lab.notes.data.dao.CategoryRepository;
import rs.lab.notes.services.CategoryService;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import rs.lab.notes.exceptions.ObjectNotExistException;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category createCategory(String name) {
        var old = categoryRepository.findByName(name);
        if (old.isPresent()) {
            throw new ObjectExistException("category");
        }
        var newCategory = Category.builder()
                .name(name)
                .build();

        return categoryRepository.save(newCategory);
    }

    @Override
    public Page<Category> listCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> modifyCategory(Long id, String name) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return Optional.empty();
        }

        category.setName(name);
        categoryRepository.save(category);

        return Optional.of(category);
    }

    @Override
    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new ObjectNotExistException("category"));
        categoryRepository.deleteById(category.getId());
    }

    private static Specification<Category> nameContains(String query) {
        return (category, cq, cb) -> cb.like(cb.lower(category.get("name")), cb.lower(cb.literal("%" + query + "%")));
    }

    @Override
    public Page<Category> searchByName(String query, Pageable pageable) {
        return this.categoryRepository.findAll(nameContains(query), pageable);
    }
}
