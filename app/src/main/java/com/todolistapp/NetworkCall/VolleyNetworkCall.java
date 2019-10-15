package com.todolistapp.NetworkCall;

public class VolleyNetworkCall {

    public String BaseURL="https://www.alperenyukselaltug.com/api/";

    public String UserLoginUrl = BaseURL+ "users/userlogin";
    public String UserRegisterUrl = BaseURL+ "users/userregister?UserMail=";

    public String getBaseURL() {
        return BaseURL;
    }

    public String getUserLoginUrl() {
        return UserLoginUrl;
    }

    public String getUserRegisterUrl() {
        return UserRegisterUrl;
    }




}

