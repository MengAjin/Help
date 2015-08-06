package com.example.Class;

import android.app.Application;


public class Person extends Application {
    private int id;
    private long account;
    private int phone = 0;
    private int gender;
    private int age = 0;
    private String nick;
    private String avata;
    private int occupation;//ְҵ
    private String location;//סַ
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
    public long getAccount(){
        return account;
    }
    public void setAccount(long account_){
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
    public int getOccupation(){return occupation;}
    public void setOccupation(int o){occupation = o;}
    public String getLocation(){return location;}
    public void setLocation(String l){location = l;}
}
