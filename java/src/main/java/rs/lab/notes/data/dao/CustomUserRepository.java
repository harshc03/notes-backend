package rs.lab.notes.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.UserDetailsView;

public interface CustomUserRepository {

    Page<UserDetailsView> findAllWithDetails(Pageable pageable);
}
