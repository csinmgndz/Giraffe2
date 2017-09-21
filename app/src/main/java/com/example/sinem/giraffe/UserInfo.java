package com.example.sinem.giraffe;

import android.net.Uri;

/**
 * Created by sinem on 12.9.2017.
 */

public class UserInfo {

    String userName;
    String userEmail;
    String nameSurname;
    Uri profilePhoto;


    public UserInfo() {
    }

    public UserInfo(String userName, String userEmail, String nameSurname,Uri profilePhoto) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.nameSurname = nameSurname;
        this.profilePhoto=profilePhoto;
    }
}