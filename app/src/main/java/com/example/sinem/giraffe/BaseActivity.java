package com.example.sinem.giraffe;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by sinem on 12.9.2017.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    public void showProgressDialog()
    {
        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}