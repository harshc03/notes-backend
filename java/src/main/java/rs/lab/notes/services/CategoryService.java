package rs.lab.notes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.Category;

import java.util.Optional;

public interface CategoryService {

    Optional<Category> findCategoryById(Long id);

    Optional<Category> findCategoryByName(String name);

    Category createCategory(String name);

    Page<Category> listCategories(Pageable pageable);

    Optional<Category> modifyCategory(Long id, String name);

    void deleteCategory(Long id);

    Page<Category> searchByName(String query, Pageable pageable);
}
