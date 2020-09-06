package com.dam.t08p01.repositorio;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthGoogle {
    private FirebaseAuth firebaseAuth;
    private Context mContext;
    private boolean resultado;


    public AuthGoogle(Context context) {
        this.mContext = context;
    }

    public void registrarUsuario(String email, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("t08p01", "registro ok");
                        } else {
                            Log.i("t08p01", "registro ko");
                        }
                    }
                });
    }

    public boolean login(String email, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("t08p01", "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            resultado = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("t08p01", "signInWithEmail:failure", task.getException());
                            resultado = false;

                        }

                        // ...
                    }
                });
        return resultado;
    }

    public void borrarUsuario(String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {
                                Log.i("t08p01", "Userio borrado");
                            }
                        }
                    });
                }
            });
        }

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }


}
