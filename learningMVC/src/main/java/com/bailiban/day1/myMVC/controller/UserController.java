package com.bailiban.day1.myMVC.controller;

import com.bailiban.day1.myMVC.model.User;
import com.bailiban.day1.myMVC.myannotation.MyRequestMapping;
import com.bailiban.day1.myMVC.myannotation.MyRestController;

import java.util.ArrayList;
import java.util.List;

@MyRestController
public class UserController {

    private static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(1, "Jim"));
        userList.add(new User(2, "Lily"));
    }

    @MyRequestMapping("/get")
    public String get(int id) {
        for (User user:userList) {
            if (user.getId()==id){
                return user.getName();
            }
        }
        return "";
    }

    @MyRequestMapping("/getAll")
    public String getAll() {
        StringBuffer stringBuffer = new StringBuffer();
        for (User user:userList) {
            stringBuffer.append("id:").append(user.getId()).append(" name:").append(user.getName()).append("\r\n");
        }
        return stringBuffer.toString();
    }
    @MyRequestMapping("/get2")
    public String get2(int id,String name){
        return id+":"+name;
    }

    @MyRequestMapping("/addUser")
    public String addUser(int id,String name){
        User user = new User(id, name);
        userList.add(user);
        return getAll();
    }
}
