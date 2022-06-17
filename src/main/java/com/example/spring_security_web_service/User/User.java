package com.example.spring_security_web_service.User;

import com.example.spring_security_web_service.Security.Secret.Secret;
import com.example.spring_security_web_service.User.Role.Role;
import com.example.spring_security_web_service.User.Role.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/////////////////////////////////////////////////////////////////

/**
 * User class
 */
@Entity  // Tells Hibernate to make a table out of this class
@Table(name = "user")
public class User implements UserInterface {
    private final static Logger log = LoggerFactory.getLogger(User.class);
    ///////////////////////////////////////////////////////
    @Id
    @Column(  // customize this field to support unique key
            name = "username",
            nullable = false,
            columnDefinition = "varchar(255)",
            unique = true)
    private String username;
    private String name;
    private boolean enabled;
    private boolean credentialsNotExpired;
    private boolean accountNotLocked;
    private boolean accountNotExpired;
    @ManyToOne(
            targetEntity = Secret.class,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumn(
            name = "password",
            nullable = false,
            columnDefinition = "bigint",
            referencedColumnName = "secret_id")
    private Secret password;
    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender",
            columnDefinition = "varchar(8)"
    )
    private Gender gender;
    @Column(
            name = "dob",
            columnDefinition = "date"
    )
    private LocalDate dob;
    @Column(
            name = "profession",
            columnDefinition = "varchar(255)"
    )
    private String profession;
    @ManyToMany(
            targetEntity = Role.class,
            fetch = FetchType.EAGER,
            cascade = { CascadeType.ALL })
    @JoinTable(name = "User_Roles",
            joinColumns = @JoinColumn (
                    name = "username",
                    referencedColumnName = "username",
                    columnDefinition = "varchar(255)",
                    nullable = false,
                    table = "user"),
            inverseJoinColumns = @JoinColumn(
                    name = "name",
                    referencedColumnName = "name",
                    columnDefinition = "varchar(255)",
                    nullable = false,
                    table = "role")
    )
    private Set<Role> roles;
   
    ///////////////////////////////////////////////////////

    /**
     * Default Constructor.
     */
    public  User(){}

     /**
     * Create Student User. All but [id] Args Constructor
     * @param name
     * @param username
     * @param password
     * @param gender
     * @param dob
     * @param profession
     * @param roles
     */
    public User(
            String name,
            String username,
            Secret password,
            Gender gender,
            LocalDate dob,
            String profession,
            Set<Role> roles
    ) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.profession = profession;
        this.roles = roles;
        this.enabled = true;
        this.credentialsNotExpired = true;
        this.accountNotExpired = true;
        this.accountNotLocked = true;

    }
    
    /**
     * Assigns the given user
     * @param user
     */
    public User(User user){
        this.setName(user.getName());
        this.setPassword(user.getPasswordClass());
        this.setRoles(user.getRoles());
        this.setDob(user.getDob());
        this.setUsername(user.getUsername());
        this.setGender(user.getGender());
        this.setProfession(user.getProfession());
        this.enabled = true;
        this.credentialsNotExpired = true;
        this.accountNotExpired = true;
        this.accountNotLocked = true;


    }

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     */
    public User(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password.setPassword(password);
        this.roles = setAuthorities(authorities);
        this.enabled = true;
        this.credentialsNotExpired = true;
        this.accountNotExpired = true;
        this.accountNotLocked = true;

    }

    public User(
            String username,
            Secret password,
            Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.roles = setAuthorities(authorities);
        this.enabled = true;
        this.credentialsNotExpired = true;
        this.accountNotExpired = true;
        this.accountNotLocked = true;

    }
    /**
     * Construct the <code>User</code> with the details required by
     * {@link DaoAuthenticationProvider}.
     * @param username the username presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     * expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public User(String username, String password, boolean enabled, boolean accountNonExpired,
                boolean credentialsNonExpired, boolean accountNonLocked,
                Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.setUsername(username);
        this.password.setPassword(password);
        this.enabled = true;
        this.credentialsNotExpired = true;
        this.accountNotExpired = true;
        this.accountNotLocked = true;
        this.setAuthorities(authorities);
    }


    public Set<Role> setAuthorities(Set<GrantedAuthority> authorities){
        this.roles = (Set<Role>) authorities.stream().map(
                x -> new Role(UserRole.valueOfLabel( x.getAuthority().replaceAll("ROLE_","")),
                        UserRole.valueOfLabel( x.getAuthority().replaceAll("ROLE_","")).name())
        );
        return this.roles;
    }
    public Set<Role> setAuthorities(Collection<? extends GrantedAuthority> authorities){
        this.roles = (Set<Role>) authorities.stream().map(
                x -> new Role(UserRole.valueOfLabel( x.getAuthority().replaceAll("ROLE_","")),
                        UserRole.valueOfLabel( x.getAuthority().replaceAll("ROLE_","")).name())
        );
        return this.roles;
    }
    /**
     * Get Granted Authorities
     * @return a Collection GrantedAuthority
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        return this.getRoles().stream().map(
                    x -> new SimpleGrantedAuthority("ROLE_" + x.getRole().name())).collect(Collectors.toList());
    }

    @Override
    public String getName(){
        return this.name;
    }

    public String getUsername(){
        return this.username;
    }

    /**
     * Get the user secret
     *
     * @return the user secret
     */

    public String getPassword(){return this.password.getPasswordToString();}
    public Secret getPasswordClass(){return this.password;}
    /**
     * Get the user gender
     *
     * @return the user gender
     */
    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public void  setRole(Role role){
        this.roles.addAll((Collection<? extends Role>) role);
    }

    @Override
    public Integer getAge() {
        return Period.between(this.getDob(), LocalDate.now()).getYears();
    }

    /**
     * Get the user dob
     *
     * @return the user dob
     */
    @Override
    public LocalDate getDob() {
        return this.dob;
    }

    /**
     * Get the user profession
     *
     * @return the user profession
     */
    @Override
    public String getProfession() {
        return this.profession;
    }


    /**
     * Set username
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set user email
     *
     * @param email
     */
    public void setUsername(String email) {
        this.username = email;
    }

    /**
     * Set user secret
     *
     * @param secret
     */
    @Override
    public void setPassword(Secret secret) {
        this.password = secret;
    }

    /**
     * Set user gender
     *
     * @param gender
     */
    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Set user dob
     *
     * @param dob
     */
    @Override
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    /**
     * Set user profession
     *
     * @param profession
     */
    @Override
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * Set user roles
     *
     * @param roles
     */
    @Override
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Get the user roled
     *
     * @return the user roles
     */
    @Override
    public Set<Role> getRoles() {
        return this.roles;
    }


    @Override
    public String toString() {
        String json = "\n{" +
                ",\n\t\"name\":\"" + this.getName() + "\"" +
                ",\n\t\"email\":\"" + this.getUsername() + "\"" +
                ",\n\t\"secret\":\"" + this.getPassword() + "\"" +
                ",\n\t\"gender\":\"" + this.getGender() + "\"" +
                ",\n\t\"dob\":\"" + this.getDob() + "\"" +
                ",\n\t\"profession\":\"" + this.getProfession() + "\"" +
                ",\n\t\"roles\":\"" + this.getRoles() + "\"" +
                "\n}";
        log.info(json);
        return json;
    }


    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isCredentialsNotExpired() {
        return this.credentialsNotExpired;
    }

    public boolean isAccountNotLocked() {
        return this.accountNotLocked;
    }

    public boolean isAccountNotExpired() {
        return this.accountNotExpired;
    }

    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
//        this.credentialsNotExpired = credentialsNotExpired;
    }

    public void setAccountNotLocked(boolean accountNotLocked) {
//        this.accountNotLocked = accountNotLocked;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
//        this.accountNotExpired = accountNotExpired;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (!Objects.equals(this$username, other$username)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        if (this.isEnabled() != other.isEnabled()) return false;
        if (this.isCredentialsNotExpired() != other.isCredentialsNotExpired()) return false;
        if (this.isAccountNotLocked() != other.isAccountNotLocked()) return false;
        if (this.isAccountNotExpired() != other.isAccountNotExpired()) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (!Objects.equals(this$password, other$password)) return false;
        final Object this$gender = this.getGender();
        final Object other$gender = other.getGender();
        if (!Objects.equals(this$gender, other$gender)) return false;
        final Object this$dob = this.getDob();
        final Object other$dob = other.getDob();
        if (!Objects.equals(this$dob, other$dob)) return false;
        final Object this$profession = this.getProfession();
        final Object other$profession = other.getProfession();
        if (!Objects.equals(this$profession, other$profession))
            return false;
        final Object this$roles = this.getRoles();
        final Object other$roles = other.getRoles();
        if (!Objects.equals(this$roles, other$roles)) return false;
        return true;
    }

    public boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + (this.isEnabled() ? 79 : 97);
        result = result * PRIME + (this.isCredentialsNotExpired() ? 79 : 97);
        result = result * PRIME + (this.isAccountNotLocked() ? 79 : 97);
        result = result * PRIME + (this.isAccountNotExpired() ? 79 : 97);
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $gender = this.getGender();
        result = result * PRIME + ($gender == null ? 43 : $gender.hashCode());
        final Object $dob = this.getDob();
        result = result * PRIME + ($dob == null ? 43 : $dob.hashCode());
        final Object $profession = this.getProfession();
        result = result * PRIME + ($profession == null ? 43 : $profession.hashCode());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        return result;
    }
}
