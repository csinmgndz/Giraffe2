package com.example.sinem.giraffe;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpRegisterActivity extends BaseActivity {

    private static final String TAG="Sign in Activity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText userNameSurname,userName,userEmail,userPassword;
    private Button saveUser,goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };




       userNameSurname= (EditText) findViewById(R.id.adSoyad);
        userName=(EditText) findViewById(R.id.txt_userName);
        userEmail=(EditText)findViewById(R.id.email_address);
        userPassword=(EditText)findViewById(R.id.txt_password);

        saveUser=(Button) findViewById(R.id.btn_save_user);
        goLogin=(Button) findViewById(R.id.btn_go_login);

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();

            }
        });

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login= new Intent(SignUpRegisterActivity.this,LoginActivity.class);
                startActivity(login);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null){
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(){
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:onComplete: "+task.isSuccessful());
                            Intent login= new Intent(SignUpRegisterActivity.this,MainActivity.class);
                            startActivity(login);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpRegisterActivity.this,
                                    "Kaydınız yapılamadı... Tekrar deneyiniz!",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }




}


