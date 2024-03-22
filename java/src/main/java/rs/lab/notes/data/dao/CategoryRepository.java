package rs.lab.notes.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.Category;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>, PagingAndSortingRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
}
