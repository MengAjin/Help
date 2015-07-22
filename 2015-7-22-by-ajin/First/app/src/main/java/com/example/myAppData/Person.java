package com.example.myAppData;

import android.app.Application;


public class Person extends Application {
    private int id;
    private int account;
    private int phone = 0;
    private int gender;
    private int age = 0;
    private String nick;
    private String avata;
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    public int getId(){
        return id;
    }

    public void setId(int id_) {
        id = id_;
    }
    public int getAccount(){
        return account;
    }
    public void setAccount(int account_){
        account = account_;
    }
    public void setPhone(int s){
        phone = s;
    }
    public int getPhone(){
        return phone;
    }
    public void setGender(int s){
        gender = s;
    }
    public int getGender(){
        return gender;
    }
    public void setAge(int s){
        age = s;
    }
    public int getAge(){
        return age;
    }

    public void setNick(String s) {
        nick = s;
    }
    public String getNick(){
        return nick;
    }

}
