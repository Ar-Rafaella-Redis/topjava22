package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class UserUtil {

    public static final int ADMIN_ID = 1;
    public static final int USER1_ID = 2;
    public static final int USER2_ID = 3;

    public static final List<User> users = Arrays.asList(
         //   User(Integer id, String name, String email, String password, Role role, Role... roles)
            new User(ADMIN_ID, "user1", "user1@mail.ru", "user1", Role.USER),
            new User(USER1_ID, "user2", "user2@mail.ru", "user2", Role.USER),
            new User(USER2_ID, "admin", "admin@mail.ru", "admin", Role.ADMIN, Role.USER));

    public static int getCaloriesByUserId(int userId){
        User user = users.stream().filter(u -> userId==u.getId()).findFirst().orElse(null);
        return (user == null)? 0: user.getCaloriesPerDay();
    }
}
