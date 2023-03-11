package com.group9.firestore.service;

import com.google.cloud.firestore.*;
import com.group9.firestore.document.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final Logger LOGGER = Logger.getLogger("UserService");
    private final Firestore firestore;

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String createUser(User user) throws ExecutionException, InterruptedException {
        LOGGER.info("Create user");
        var result = createOrUpdateUser(user);
        LOGGER.info("User created at time " + result.getUpdateTime());
        return user.getId();
    }

    public void updateUser(User user) throws ExecutionException, InterruptedException {
        LOGGER.info("Update user");
        var result = createOrUpdateUser(user);
        LOGGER.info("User updated at time " + result.getUpdateTime());
    }

    public User getUserById(final String id) throws ExecutionException, InterruptedException {
        LOGGER.info("Getting user by id");
        var result = userCollection()
                .document(id)
                .get();
        return result.get().toObject(User.class);
    }

    public List<User> getUsers(
            final String firstName,
            final String lastName,
            final String streetName,
            final String city
    ) throws ExecutionException, InterruptedException {
        LOGGER.info("Get users based on search parameter");
        var result = filterQuery(userCollection(), firstName, lastName, streetName, city).get();
        return result.get().toObjects(User.class);
    }

    public void deleteAllUsers() {
        firestore.recursiveDelete(userCollection());
        LOGGER.info("Deleted all users");
    }

    public void deleteUserById(final String id) {
        firestore.recursiveDelete(userCollection().document(id));
        LOGGER.info("Deleted user with id" + id);
    }

    public void listenToDocuments(final String id) {
        final DocumentReference docRef = firestore.collection("user-collection").document(id);
        docRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                LOGGER.info("Listen failed." + error);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                LOGGER.info("Current data: " + snapshot.getData());
            } else {
                LOGGER.info("Current data: null");
            }
        });
    }

    private CollectionReference userCollection() {
        return firestore.collection("user-collection");
    }

    private Query filterQuery(
            final Query result,
            final String firstName,
            final String lastName,
            final String streetName,
            final String city
    ) {
        Query query = result;
        if (firstName != null) {
            query = query.whereEqualTo("firstName", firstName);
        }
        if (lastName != null) {
            query = query.whereEqualTo("lastName", lastName);
        }
        if (streetName != null) {
            query = query.whereEqualTo("address.streetName", streetName);
        }
        if (city != null) {
            query = query.whereEqualTo("address.city", city);
        }
        return query;
    }

    private WriteResult createOrUpdateUser(User user) throws ExecutionException, InterruptedException {
        return userCollection()
                .document(user.getId())
                .set(user)
                .get();
    }
}
