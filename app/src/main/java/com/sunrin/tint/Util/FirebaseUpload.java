package com.sunrin.tint.Util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUpload {
    // TODO: Complete Class
    private static final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageReference = storage.getReference();
}
