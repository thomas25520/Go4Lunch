package com.example.go4lunch.api;

import com.example.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Dutru Thomas on 24/09/2019.
 */
public class UserHelper {
    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createUser(String username, String urlPicture, String email) {
        User userToCreate = new User(username, urlPicture, email, false);
        return UserHelper.getUsersCollection().document(email).set(userToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String email){
        return UserHelper.getUsersCollection().document(email).get();
    }

//    // --- UPDATE ---
//    public static Task<Void> updateUsername(String username, String uid) {
//        return UserHelper.getUsersCollection().document(uid).update("username", username);
//    }

    public static Task<Void> updateIsEating(String uid, Boolean isEating) {
        return UserHelper.getUsersCollection().document(uid).update("isEating", isEating);
    }

//    // --- DELETE ---
//    public static Task<Void> deleteUser(String uid) {
//        return UserHelper.getUsersCollection().document(uid).delete();
//    }
}
