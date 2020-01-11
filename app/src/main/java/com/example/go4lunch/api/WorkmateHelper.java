package com.example.go4lunch.api;

import com.example.go4lunch.Constant;
import com.example.go4lunch.data.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Dutru Thomas on 24/09/2019.
 */
public class WorkmateHelper {
    private static final String COLLECTION_NAME = "workmates";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

        // --- CREATE ---
    public static Task<Void> createWorkmate(String username, String urlPicture, String email) {
        Workmate workmateToCreate = new Workmate(username, "", urlPicture, email, false,"");
        return WorkmateHelper.getWorkmatesCollection().document(email).set(workmateToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getWorkmate(String userUid) {
        return WorkmateHelper.getWorkmatesCollection().document(userUid).get();
    }

    public static String getStringInfoFrom(String information, DocumentSnapshot document) {
        return document.getString(information);
    }
    public static boolean getBooleanInfoFrom(String information, DocumentSnapshot document) {
        return document.getBoolean(information);
    }

    // --- UPDATE ---
    public static Task<Void> updateWorkmateName(String username, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update("name", username);
    }

    public static Task<Void> updateWorkmatePicture(String picture, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update("pictureUrl", picture);
    }
    public static Task<Void> updateWorkmateRestaurantId(String restaurantId, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update(Constant.RESTAURANT_ID, restaurantId);
    }

    public static Task<Void> updateWorkmateRestaurantName(String restaurantName, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update(Constant.RESTAURANT_NAME, restaurantName);
    }

    public static Task<Void> updateIsWorkmateEating(String uid, Boolean isEating) {
    return WorkmateHelper.getWorkmatesCollection().document(uid).update("eating", isEating);
    }

    public static void isWorkmateExist(OnCompleteListener<QuerySnapshot> listener) {
        WorkmateHelper.getWorkmatesCollection()
                .get()
                .addOnCompleteListener(listener);
    }

    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).delete();
    }
}
