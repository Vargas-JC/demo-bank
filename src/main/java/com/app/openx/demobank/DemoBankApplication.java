package com.app.openx.demobank;

import com.app.openx.demobank.entity.User;
import com.app.openx.demobank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@SpringBootApplication
public class DemoBankApplication implements CommandLineRunner {
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(DemoBankApplication.class, args);
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Edwin Vargas", "71633601", LocalDateTime.now()));
        userList.add(new User(2, "JC Alvarado", "71633609", LocalDateTime.now()));
        userList.add(new User(3, "Ivan Vargas", "71633600", LocalDateTime.now()));

        //Add users
        System.out.println("\n Saving");
        List<User> usersSaved = userService.saveUsers(userList);
        usersSaved.parallelStream()
                .forEach(System.out::println);

        //FindById
        System.out.println("\n Find By Id");
        User user = userService.findUserById(2);
        System.out.println(user.toString());

        //Delete By Id
        System.out.println("\n Delete By ID");
        boolean deleted = userService.deleteUser(2);
        System.out.println("Deleted id 2 -> " + deleted);

        //FindAll
        System.out.println("\n Find All");
        List<User> userListReceived = userService.findAllUsers();
        userListReceived.forEach(System.out::println);
    }
}
