package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InteractApi {
    public static final String URI = "https://jsonplaceholder.typicode.com";
    public static final Gson gson = new Gson();

    public JsonObject createUser(User user) throws IOException {
        String userUri = URI +"/users";
        String userJson = gson.toJson(user);

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(userUri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(userJson));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        return gson.fromJson(responseBody, JsonObject.class);

    }

    public JsonObject updateUser(int userId) throws IOException {
        String userUri = URI +"/users/" + userId;
        String userJson = gson.toJson(getUserWithID(userId));

        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(userUri);
        httpPut.setHeader("Content-Type","application/json");
        httpPut.setEntity(new StringEntity(userJson));

        HttpResponse response = httpClient.execute(httpPut);
        String responseBody = EntityUtils.toString(response.getEntity());

        return gson.fromJson(responseBody, JsonObject.class);
    }

    public boolean deleteUser(int userId) throws IOException {
        String userUri = URI +"/users/" + userId;

        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(userUri);

        HttpResponse response = httpClient.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();

        return statusCode >= 200 && statusCode < 300;
    }

    public User[] getAllUsers() throws IOException {
        String userUri = URI +"/users";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userUri);

        HttpResponse response = httpClient.execute(httpGet);

        String responseBody = EntityUtils.toString(response.getEntity());

        return gson.fromJson(responseBody, User[].class);
    }

    public User getUserWithID(int userId) throws IOException {
        String userUri = URI +"/users/" + userId;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userUri);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        return gson.fromJson(responseBody, User.class);

    }

    public User[] getUserByUsername(String userName) throws IOException {
        String userUri = URI +"/users?username=" + userName;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userUri);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        return gson.fromJson(responseBody, User[].class);

    }

    public void fetchAndSaveCommentsForLastPost(int userId) throws IOException {
        String userPostsUri = URI + "/users/" + userId + "/posts";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userPostsUri);
        HttpResponse response = httpClient.execute(httpGet);

        String responseBody = EntityUtils.toString(response.getEntity());
        JsonArray userPosts = gson.fromJson(responseBody, JsonArray.class);

        if (userPosts.size() > 0) {
            String lastPostId = userPosts.get(userPosts.size() - 1).getAsJsonObject().get("id").getAsString();
            String postCommentsUri = URI + "/posts/" + lastPostId + "/comments";

            HttpGet commentsHttpGet = new HttpGet(postCommentsUri);
            HttpResponse commentsResponse = httpClient.execute(commentsHttpGet);

            String commentsResponseBody = EntityUtils.toString(commentsResponse.getEntity());

            String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
            try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
                fileWriter.write(commentsResponseBody);
            }
        }
    }

    public JsonObject[] getOpenTasksForUser(int userId) throws IOException {
        String userTasksUri = URI + "/users/" + userId + "/todos";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userTasksUri);
        HttpResponse response = httpClient.execute(httpGet);

        String responseBody = EntityUtils.toString(response.getEntity());
        JsonArray userTasks = gson.fromJson(responseBody, JsonArray.class);

        List<JsonObject> openTasks = new ArrayList<>();
        for (JsonElement taskElement : userTasks) {
            JsonObject task = taskElement.getAsJsonObject();
            if (!task.get("completed").getAsBoolean()) {
                openTasks.add(task);
            }
        }

        return openTasks.toArray(new JsonObject[0]);
    }



}
