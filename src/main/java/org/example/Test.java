package org.example;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        InteractApi api = new InteractApi();

        try {
            // Створення нового користувача
            User newUser = new User("Jhon", "JohnJoe", "john.doe@example.com");
            JsonObject createdUser = api.createUser(newUser);
            System.out.println("Created User: " + createdUser);

            // Отримання оновленого користувача з бази даних перед оновленням
            User userToUpdate = api.getUserWithID(createdUser.get("id").getAsInt());

            // Оновлення даних користувача та виклик updateUser
            userToUpdate.setUserName("UpdatedUsername");
            userToUpdate.setEmail("updated@example.com");
            JsonObject updatedResult = api.updateUser(userToUpdate);
            System.out.println("Updated User: " + updatedResult);

            // Видалення користувача
            boolean deleted = api.deleteUser(11);
            System.out.println("User deletion status: " + deleted);

            // Отримання всіх користувачів
            User[] allUsers = api.getAllUsers();
            System.out.println("All Users: " + Arrays.toString(allUsers));

            // Отримання користувача за ID
            User userById = api.getUserWithID(1);
            System.out.println("User with ID 1: " + userById);

            // Отримання користувачів за ім'ям користувача
            User[] usersByUsername = api.getUserByUsername("Bret");
            System.out.println("Users with username 'Bret': " + Arrays.toString(usersByUsername));

            // Збереження коментарів останнього посту користувача
            api.fetchAndSaveCommentsForLastPost(1);

            // Отримання невиконаних завдань користувача
            JsonObject[] openTasks = api.getOpenTasksForUser(1);
            System.out.println("Open tasks for user: " + Arrays.toString(openTasks));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
