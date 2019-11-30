package com.example.go4lunch.api;

import com.example.go4lunch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    public static Task<DocumentSnapshot> getUser(String userUid) {
        return UserHelper.getUsersCollection().document(userUid).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("userName", username);
    }

    public static Task<Void> updateUserPicture(String picture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("userPicture", picture);
    }

    public static Task<Void> updateIsEating(String uid, Boolean isEating) {
    return UserHelper.getUsersCollection().document(uid).update("isEating", isEating);
    }

    public static void isUserExist(OnCompleteListener<QuerySnapshot> listener) {
        UserHelper.getUsersCollection()
                .get()
                .addOnCompleteListener(listener);
    }

//    // TODO: 29/11/2019 Exo
//    public static void isUserExist(String userMailAddress, String userName, String userProfilePicture, Context context, Listener startActivity) {
//        UserHelper.getUsersCollection()
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) { // they are document to parse
//                        for (QueryDocumentSnapshot document : task.getResult())
//                            if (document.getId().equals(userMailAddress)) {  // Is the document exist we leave // id == nom du doc == mail
//                                startActivity();
//                                return;
//                            }
//                        UserHelper.createUser(userName, userProfilePicture, userMailAddress); // Create User on DB
//                    } else
//                        Log.d("tag", "Error getting documents: ", task.getException());
//
//                startActivity();
//
//                });
//    }
//
//    @Override
//    public void startActivity() {
//
//    }
//
//    // --- DELETE ---
//    public static Task<Void> deleteUser(String uid) {
//        return UserHelper.getUsersCollection().document(uid).delete();
//    }
}
