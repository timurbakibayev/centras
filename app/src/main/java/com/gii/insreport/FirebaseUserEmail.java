package com.gii.insreport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timur_hnimdvi on 07-Sep-16.
 */
public class FirebaseUserEmail {
    public String email = "";
    public String id = "";
    public Date lastLogin = new Date();
    public String lastLoginAndroid = "";
    public String token = "";
    public Map<String,String> devices = new HashMap<>();
    public FirebaseUserEmail() {

    };

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getLastLoginAndroid() {
        return lastLoginAndroid;
    }

    public String getToken() {
        return token;
    }
}
