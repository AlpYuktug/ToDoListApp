package com.todolistapp.NetworkCall;

public class VolleyNetworkCall {

    public String BaseURL="https://www.alperenyukselaltug.com/api/";

    public String UserLoginUrl = BaseURL+ "user/userlogin";
    public String UserRegisterUrl = BaseURL+ "user/userregister?UserMail=";

    public String ToDoListUrl = BaseURL+ "todo/todolist";
    public String ToDoListItemUrl = BaseURL+ "todo/todolistitem";
    public String ToDoListAddUrl = BaseURL + "todo/addtodolist";

    public String ToDoListItemUpdateCompletedUrl = BaseURL+ "todo/updatetodoitemcomplated";
    public String ToDoListItemDeleteddUrl = BaseURL+ "todo/deletetodoitem";
    public String ToDoListDeletedUrl = BaseURL+ "todo/deletetodolist";

    public String ToDoListItemAddExistUrl = BaseURL+ "todo/addtooitemexist";

    public String ToDoListItemOrderCategoryUrl = BaseURL+ "todo/ordercategory";


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

    public String getToDoListAddUrl() {
        return ToDoListAddUrl;
    }

    public String getToDoListItemUpdateComplatedUrl() {
        return ToDoListItemUpdateCompletedUrl;
    }

    public String getToDoListItemDeleteddUrl() {
        return ToDoListItemDeleteddUrl;
    }

    public String getToDoListDeleteddUrl() {
        return ToDoListDeletedUrl;
    }

    public String getToDoListItemAddExistUrl() {
        return ToDoListItemAddExistUrl;
    }

    public String getToDoListItemOrderCategoryUrl() {
        return ToDoListItemOrderCategoryUrl;
    }

}

