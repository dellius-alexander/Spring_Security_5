package com.example.spring_security_web_service.User.Role;
// LOGGING CLASSES

import com.example.spring_security_web_service.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/////////////////////////////////////////////////////////////////////

/**
 * This class defines the USER ROLE.
 * {@link UserRole}:
 *   ADMIN,
 *   STUDENT,
 *   FACULTY,
 *   GUEST,
 *   SUPERUSER
 */
@Entity
@Table(name = "role")
public class Role implements RoleInterface {

    // Define a logger instance and log what you want.
	private static final Logger log = LoggerFactory.
    getLogger(Role.class);
    /////////////////////////////////////////////////////////////////
    @Id
    @Column(name = "name",
            unique = true,
            nullable = false,
            columnDefinition = "varchar(255)"
    )
    @Enumerated(EnumType.STRING)
    private UserRole name;
    private String description;
    @ManyToMany(
            mappedBy = "roles",
            cascade = {CascadeType.ALL}
    )
    private Set<User> users;

    public Role(UserRole name) {
        this.name = name;
        log.info("Role: {}", this);
    }

    public Role(String role,String description){
        this.name = UserRole.valueOfLabel(role);
        this.description = description;
    }

    public Role(UserRole name,String description) {
        this.name = name;
        this.description = description;
    }

    public Role() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public UserRole getRole() {
        return this.name;
    }

    @Override
    public void setRole(UserRole name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description){
        this.description = description; }

    @Override
    public String getDescription(){ return this.description; }

    @Override
    public Role getRoleByName(UserRole name) {
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(this.name, role.name);
    }

    
    @Override
    public boolean equals(UserRole name1, UserRole name2){
        return  Pattern.compile(Pattern.quote(name1.name()),
                Pattern.CASE_INSENSITIVE).matcher(name2.name()).find();
    }

    @Override
    public boolean equals(
            Role role1,
            Role role2
    ){
        return  Pattern.compile(Pattern.quote(role1.getRole().name()),
                        Pattern.CASE_INSENSITIVE).matcher(role2.getRole().name()).find();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description);
    }

    @Override
    public String toString() {
        String json = String.format("\n\t{" +
            "\n\t\t\"name\":\"" + getRole() + "\"" +
            ",\n\t\t\"description\":\"" + getDescription() + "\"" +
            "\n\t}");
        log.info("\nRole: {}\n",json);
        return json;
    }

}
