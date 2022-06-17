package com.example.spring_security_web_service.User.Role;

import java.io.Serializable;

public interface RoleInterface extends Serializable {

   UserRole getRole();

    void setRole(UserRole name);

    void setDescription(String description);

    String getDescription();


    Role getRoleByName(UserRole name);

    @Override
    boolean equals(Object o);

    boolean equals(UserRole name1, UserRole name2);

    boolean equals(Role role1, Role role2);

    @Override
    int hashCode();

    @Override
    String toString();
}
