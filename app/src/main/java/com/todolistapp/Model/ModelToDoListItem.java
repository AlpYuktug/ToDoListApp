package com.todolistapp.Model;

import java.util.Date;

public class ModelToDoListItem {

    public String   ToDoListItemTopic;
    public String   ToDoListItemDescription;
    public Integer  ToDoListNumber;
    public Integer  ToDoListItemNumber;
    public Integer  ToDoListItemCheck;
    public String     ToDoListItemDeadLine;


    public ModelToDoListItem(Integer ToDoListNumber,Integer ToDoListItemNumber,
                             Integer ToDoListItemCheck,String ToDoListItemTopic,String ToDoListItemDescription, String ToDoListItemDeadLine) {
        this.ToDoListNumber = ToDoListNumber;
        this.ToDoListItemNumber = ToDoListItemNumber;
        this.ToDoListItemCheck = ToDoListItemCheck;
        this.ToDoListItemTopic = ToDoListItemDescription;
        this.ToDoListItemDescription = ToDoListItemDescription;
        this.ToDoListItemDeadLine = ToDoListItemDeadLine;
    }

    public String getToDoListItemTopic() {
        return ToDoListItemTopic;
    }

    public void setToDoListItemTopic(String ToDoListItemTopic) {
        this.ToDoListItemTopic = ToDoListItemTopic;
    }

    public String getToDoListItemDescription() {
        return ToDoListItemDescription;
    }

    public void setToDoListItemDescription(String ToDoListItemDescription) {
        this.ToDoListItemDescription = ToDoListItemDescription;
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

    public String getToDoListItemDeadLine() {
        return ToDoListItemDeadLine;
    }

    public void setToDoListItemDeadLine(String ToDoListItemDeadLine) {
        this.ToDoListItemDeadLine = ToDoListItemDeadLine;
    }

}