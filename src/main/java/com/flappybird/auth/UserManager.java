package com.flappybird.auth;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Manages persistent user storage and authentication using a local data file
public class UserManager {

    private static final String FILE_NAME = "flappybird_users.dat";

    // Saves the new user or updates the high score of an existing user in the storage file
    public static void saveUser(User user) {
        try {
            List<User> users = loadUsers();
            
            // Check if user already exists
            boolean userExists = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(user.getUsername())) {

                    if (user.getScore() > users.get(i).getScore()) {
                        users.get(i).setScore(user.getScore());
                    }
                    userExists = true;
                    break;
                }
            }

            // If new user, add to list
            if (!userExists) {
                users.add(user);
            }

            // Write all users back to file using RandomAccessFile
            try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
                raf.setLength(0); // Clear the file
                for (User u : users) {
                    writeUserRecord(raf, u);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reads and parses the entire user registry from the local data file
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                User user = parseUserRecord(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (FileNotFoundException e) {
            // Return an empty list gracefully if the file hasn't been created yet
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Authenticates a user by checking username and password.
    public static User login(String username, String password) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

     // Writes a single user record to RandomAccessFile.
    private static void writeUserRecord(RandomAccessFile raf, User user) throws IOException {
        String record = user.getUsername() + "," + user.getPassword() + "," + user.getScore();
        raf.writeBytes(record + "\n");
    }

    // Extracts comma-separated tokens from a single text line to instantiate a User object
    private static User parseUserRecord(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 3) {
                return null; // Invalid format
            }

            String username = parts[0].trim();
            String password = parts[1].trim();
            int score = Integer.parseInt(parts[2].trim());

            return new User(username, password, score);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}