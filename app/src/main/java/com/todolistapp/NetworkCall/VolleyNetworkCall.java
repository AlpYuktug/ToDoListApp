package com.todolistapp.NetworkCall;

public class VolleyNetworkCall {

    public String BaseURL="https://www.alperenyukselaltug.com/api/";

    public String UserLoginUrl = BaseURL+ "user/userlogin";
    public String UserRegisterUrl = BaseURL+ "user/userregister?UserMail=";

    public String ToDoListUrl = BaseURL+ "todo/todolist";
    public String ToDoListItemUrl = BaseURL+ "todo/todolistitem";


    public String getBaseURL() {
        return BaseURL;
    }

    public String getUserLoginUrl() {
        return UserLoginUrl;
    }

    public String getUserRegisterUrl() {
        return UserRegisterUrl;
    }

    public String getToDoListUrl() {
        return ToDoListUrl;
    }

    public String getToDoListItemUrl() {
        return ToDoListItemUrl;
    }



}

