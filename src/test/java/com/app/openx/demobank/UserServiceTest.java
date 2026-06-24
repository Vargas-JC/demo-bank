package com.app.openx.demobank;


import com.app.openx.demobank.entity.User;
import com.app.openx.demobank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp(){
        userService = new UserService();
    }

    @Test
    void saveUsers_DeberiaGuardarUsuariosYRetornarListaCompleta(){
        User u1 = new User();
        u1.setId(1);

        User u2 = new User();
        u1.setId(2);

        List<User> newUsers = List.of(u1, u2);

        List<User> result = userService.saveUsers(newUsers);

        assertEquals(2, result.size());
        assertTrue(result.contains(u1));
        assertTrue(result.contains(u2));
    }
}
