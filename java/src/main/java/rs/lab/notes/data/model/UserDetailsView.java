package rs.lab.notes.data.model;

import java.util.Date;
import java.util.Set;
import lombok.Getter;

@Getter
public class UserDetailsView {

    public long id;
    public String fullName;
    public String email;
    public String passwordHash;
    public boolean locked;
    public Date createdAt;
    public Date updatedAt;
    public Set<Role> roles;
    public long numberOfNotes;
    public long numberOfShared;
}
