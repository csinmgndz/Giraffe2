package com.example.sinem.giraffe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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


public abstract class LoginActivity extends BaseActivity {


    private static final String TAG="Login Activity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText userName,userEmail,userPassword;
    private Button login,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        userName=(EditText) findViewById(R.id.txt_userName);
        userEmail=(EditText)findViewById(R.id.email_address);
        userPassword=(EditText)findViewById(R.id.txt_password);
        login=(Button) findViewById(R.id.btn_login);
        signUp=(Button)findViewById(R.id.sign_up);

    }

    private void signIn(){
        String email=this.userEmail.getText().toString();
        String nameUser = this.userName.getText().toString();
        String password = this.userPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"signInWithEmail:onComplete:"+task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this,"Maalesef giriş yapılamadı...Tekrar deneyiniz!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
