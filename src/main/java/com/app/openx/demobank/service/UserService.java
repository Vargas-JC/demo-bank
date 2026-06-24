package com.app.openx.demobank.service;

import com.app.openx.demobank.entity.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    List<User> userList = new ArrayList<>();

    public List<User> saveUsers(List<User> users) {
        boolean isSaved = userList.addAll(users);
        if (isSaved){
            return userList;
        } else {
            return List.of();
        }
    }

    public List<User> findAllUsers() {
        return userList.stream()
                .toList();
    }

    public User findUserById(Integer id){
        User user = new User();
        user.setId(id);
        return userList.stream()
                .filter(n -> n.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el id."));
    }

    public boolean deleteUser(Integer id) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(id)) {
                userList.remove(i);
                return true;
            }
        }
        return false;
    }
}
