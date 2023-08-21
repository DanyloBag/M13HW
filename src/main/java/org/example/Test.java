package org.example;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        InteractApi api = new InteractApi();

        try {
           User user1 = new User("Vanja","LichKing123","lichking@email.com");

            System.out.println("api.createUser(user1) = " + api.createUser(user1));

            System.out.println("api.updateUser(1) = " + api.updateUser(1));

            System.out.println("api.deleteUser(3) = " + api.deleteUser(3));

            System.out.println("api.getUserWithID(11) = " + api.getUserWithID(9));

            System.out.println("api.getAllUsers() = " + api.getAllUsers());

            System.out.println("api.getUserByUsername(\"Bret\") = " + api.getUserByUsername("Bret"));

            api.fetchAndSaveCommentsForLastPost(3);

            api.getOpenTasksForUser(5);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
