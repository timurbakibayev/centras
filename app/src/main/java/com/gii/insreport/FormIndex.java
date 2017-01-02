package com.gii.insreport;

import java.util.Date;

/**
 * Created by Timur_hnimdvi on 02-Jan-17.
 */
public class FormIndex {
    public Date dateCreated = new Date();
    public String formId = "";
    public String userId = "";
    public String content = "";
    public String userName = "";
    public String userEmail = "";

    public FormIndex() {

    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFormId() {
        return formId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}
