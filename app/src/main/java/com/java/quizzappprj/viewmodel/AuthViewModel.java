package com.java.quizzappprj.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.java.quizzappprj.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseUser currentUser;
    private AuthRepository authRepository;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authRepository = new AuthRepository(application);
        currentUser = authRepository.getCurrentUser();
        firebaseUserMutableLiveData = authRepository.getFirebaseUserMutableLiveData();
    }

    public void signUp(String email, String pass) {
        authRepository.signUp(email, pass);
    }

    public void signIn(String email, String pass) {
        authRepository.signIn(email, pass);
    }

    public void signOut() {
        authRepository.signOut();
    }
}
