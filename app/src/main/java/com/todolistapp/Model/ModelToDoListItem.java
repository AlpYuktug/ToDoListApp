package com.todolistapp.Model;

public class ModelToDoListItem {

    public String ToDoListItemTopic;
    public Integer ToDoListNumber;
    public Integer ToDoListItemNumber;
    public Integer ToDoListItemCheck;


    public ModelToDoListItem(Integer ToDoListNumber,Integer ToDoListItemNumber,
                             Integer ToDoListItemCheck,String ToDoListItemTopic) {
        this.ToDoListNumber = ToDoListNumber;
        this.ToDoListItemNumber = ToDoListItemNumber;
        this.ToDoListItemCheck = ToDoListItemCheck;
        this.ToDoListItemTopic = ToDoListItemTopic;
    }

    public String getToDoListItemTopic() {
        return ToDoListItemTopic;
    }

    public void setToDoListItemTopic(String ToDoListItemTopic) {
        this.ToDoListItemTopic = ToDoListItemTopic;
    }


    public Integer getToDoListNumber() {
        return ToDoListNumber;
    }

    public void setToDoListNumber(Integer ToDoListNumber) {
        this.ToDoListNumber = ToDoListNumber;
    }


    public Integer getToDoListItemNumber() {
        return ToDoListItemNumber;
    }

    public void setToDoListItemNumber(Integer ToDoListItemNumber) {
        this.ToDoListItemNumber = ToDoListItemNumber;
    }


    public Integer getToDoListItemCheck() {
        return ToDoListItemCheck;
    }

    public void setToDoListItemCheck(Integer ToDoListItemCheck) {
        this.ToDoListItemCheck = ToDoListItemCheck;
    }

}