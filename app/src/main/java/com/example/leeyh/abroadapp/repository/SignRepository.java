package com.example.leeyh.abroadapp.repository;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.leeyh.abroadapp.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.leeyh.abroadapp.constants.StaticString.ALREADY_EXIST_EMAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.NOT_FORMATTED_EMAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.SUCCESS;

public class SignRepository {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private RepositoryListener mListener;

    public SignRepository() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mStorageRef = storage.getReference().child("userProfileImage");
        mDatabaseRef = database.getReference("users");
    }

    public void setRepositoryListener(RepositoryListener listener) {
        this.mListener = listener;
    }

    private void storageProfileToServer(byte[] bitmapArray, final UserModel userInfo) {
        FirebaseUser user = mAuth.getCurrentUser();
        final StorageReference myProfileStorage = mStorageRef.child(user.getUid());
        UploadTask uploadTask = myProfileStorage.putBytes(bitmapArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("에러2", "onFailure: " + e.toString());
                mListener.onTaskFinished(ERROR);
            }
        });
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("에러5", "onFailure: " + task.getException().toString());
                    mListener.onTaskFinished(ERROR);
                    return null;
                }
                return myProfileStorage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                saveUserInfoToServerDB(downloadUri, userInfo);
            }
        });
    }

    private void saveUserInfoToServerDB(Uri profileUri, UserModel userInfo) {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        userInfo.setUid(uid);
        if (profileUri != null) {
            userInfo.setUserImageUrl(profileUri.toString());
        }
        mDatabaseRef.child(uid).setValue(userInfo).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("에러3", "onFailure: " + e.toString());
                mListener.onTaskFinished(ERROR);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mListener.onTaskFinished(SUCCESS);
            }
        });
    }

    public void emailCreateUser(String password, final byte[] profileByteArray, final UserModel user) {
        mAuth.createUserWithEmailAndPassword(user.getUserId(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //storage user profile
                    if(profileByteArray != null) {
                        storageProfileToServer(profileByteArray, user);
                    } else {
                        saveUserInfoToServerDB(null, user);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mListener.onTaskFinished(NOT_FORMATTED_EMAIL);
                } else if(e instanceof FirebaseAuthUserCollisionException){
                    mListener.onTaskFinished(ALREADY_EXIST_EMAIL);
                } else {
                    mListener.onTaskFinished(ERROR);
                }
            }
        });
    }

//    public void credentialCreateUser(AuthCredential credential, final byte[] profileByteArray, final UserModel user) {
//        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                if(profileByteArray != null) {
//                    storageProfileToServer(profileByteArray, user);
//                } else {
//                    saveUserInfoToServerDB(null, user);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("에러", "onFailure: " + e.toString());
//                mListener.onTaskFinished(ERROR);
//            }
//        });
//    }

    public RepositoryListener getListener() {
        return mListener;
    }
}
