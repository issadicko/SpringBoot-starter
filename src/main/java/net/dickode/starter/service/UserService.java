package net.dickode.naima.service;


import net.dickode.naima.entity.Role;
import net.dickode.naima.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    
    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);
    List<User> getUsers();
}
