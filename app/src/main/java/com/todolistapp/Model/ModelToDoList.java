package com.todolistapp.Model;

public class ModelToDoList {

    public String ToDoListTopic;
    public Integer ToDoListNumber;

    public ModelToDoList(Integer ToDoListNumber,String ToDoListTopic) {
        this.ToDoListNumber = ToDoListNumber;
        this.ToDoListTopic = ToDoListTopic;
    }

    public String getToDoListTopic() {
        return ToDoListTopic;
    }

    public void setToDoListTopic(String ToDoListTopic) {
        this.ToDoListTopic = ToDoListTopic;
    }


    public Integer getToDoListNumber() {
        return ToDoListNumber;
    }

    public void setToDoListNumber(Integer ToDoListNumber) {
        this.ToDoListNumber = ToDoListNumber;
    }

}

