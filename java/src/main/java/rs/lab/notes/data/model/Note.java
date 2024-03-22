package rs.lab.notes.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notes", indexes = {
    @Index(columnList = "owner_id"),}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ownerId", "caption"})})
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String caption;

    @Column(nullable = false)
    private NoteStateEnum state;

    @Column(length = 4096, nullable = false)
    private String body;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date modifiedAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ownerId", nullable = false)
    private User owner;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Formula("case when (select count(*) from shared_notes sn where sn.note_id=n1_0.id)>0 then true else false end")
    private Boolean shared;
}
