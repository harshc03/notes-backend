package rs.lab.notes.data.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.dao.CustomUserRepository;
import rs.lab.notes.data.dao.RoleRepository;
import rs.lab.notes.data.model.UserDetailsView;
import rs.lab.notes.util.StringHelper;

@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final String querySQL = """
      select u.*, (select count(*) from notes n where n.owner_id=u.id) number_of_notes, (select count(*) from shared_notes sn where sn.owner_id=u.id) number_of_shared from users u
    """;

    private final String countQuerySQL = """
        select count(*) from users u
    """;

    @Autowired
    private EntityManager em;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @SuppressWarnings("unchecked")
    public Page<UserDetailsView> findAllWithDetails(Pageable pageable) {
        var sb = new StringBuilder(querySQL);

        StringHelper.appendOrderBy(sb, pageable);

        var query = em.createNativeQuery(sb.toString(), Tuple.class);

        var pageNumber = pageable.getPageNumber();
        var pageSize = pageable.getPageSize();

        query.setFirstResult(pageSize * pageNumber);
        query.setMaxResults(pageSize);

        var queryResult = (List<Tuple>) query.getResultList();

        var result = new ArrayList<UserDetailsView>();
        queryResult.forEach((var res) -> {
            var fields = UserDetailsView.class.getDeclaredFields();
            var instance = new UserDetailsView();

            Arrays.asList(fields).forEach((var field) -> {
                try {
                    if(!"roles".equals(field.getName())) {
                        field.set(instance, res.get(StringHelper.camelToSnake(field.getName())));
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            });
            result.add(instance);

            // load user roles
            instance.roles = roleRepository.findUserRoles(instance.id);
        });

        var countQuery = em.createNativeQuery(countQuerySQL);
        var totalCount = ((Long) countQuery.getSingleResult());

        return new PageImpl(result, pageable, totalCount);
    }
}
