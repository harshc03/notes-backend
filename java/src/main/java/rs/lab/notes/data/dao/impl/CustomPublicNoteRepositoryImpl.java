package rs.lab.notes.data.dao.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.dao.CustomPublicNoteRepository;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.util.StringHelper;

public class CustomPublicNoteRepositoryImpl implements CustomPublicNoteRepository {

    private final String querySQL = """
    select *, case when (select count(*) from shared_notes sn where sn.note_id=n.id)>0 then true else false end shared from (
        select n.id, n.caption, n.state, n.body, n.created_at, n.modified_at, n.category_id, n.owner_id from notes n where n.state = 1
        union
        select n.id, n.caption, n.state, n.body, n.created_at, n.modified_at, n.category_id, n.owner_id from notes n join shared_notes sn on sn.note_id = n.id where sn.user_id = :userId
    ) n
    """;

    private final String countQuerySQL = """
    select count(*) from (
        select n.id, n.caption, n.state, n.body, n.created_at, n.modified_at, n.category_id, n.owner_id from notes n where n.state = 1
        union
        select n.id, n.caption, n.state, n.body, n.created_at, n.modified_at, n.category_id, n.owner_id from notes n join shared_notes sn on sn.note_id = n.id where sn.user_id = :userId
    ) n
    """;

    @Autowired
    private EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public Page<Note> findAllByPublicOrUserId(Long userId, Pageable pageable) {
        var sb = new StringBuilder(querySQL);
        if (pageable.getSort() != null && !pageable.getSort().isEmpty()) {
            sb.append(" order by ");
            for (var order : pageable.getSort()) {
                sb.append(StringHelper.camelToSnake(order.getProperty())).append(' ').append(order.getDirection());
            }
        }

        var query = em.createNativeQuery(sb.toString(), Note.class);

        var pageNumber = pageable.getPageNumber();
        var pageSize = pageable.getPageSize();

        query.setParameter("userId", userId);
        query.setFirstResult(pageSize * pageNumber);
        query.setMaxResults(pageSize);

        var result = query.getResultList();

        var countQuery = em.createNativeQuery(countQuerySQL);
        countQuery.setParameter("userId", userId);
        var totalCount = ((Long) countQuery.getSingleResult());

        return new PageImpl(result, pageable, totalCount);
    }
}
