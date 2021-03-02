package com.bukcoja.seung.receipt_inmyhand;

import android.app.Application;

import java.util.ArrayList;

public class UserInfo extends Application {

    private String id,name,email,groupname;
    private int groupid;
    private ArrayList<String> groups=new ArrayList<>();
    private ArrayList<Integer> groupids=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroup(int groupid){ this.groupid=groupid;}

    public int getGroup(){ return groupid;}

    public void setGroupname(String name){this.groupname=name;}

    public String getGroupname(){return this.groupname;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addGroups(String group){ this.groups.add(group);}

    public void addGroupIds(int id){groupids.add(id);}
    public int getGroupId(int index){return groupids.get(index);}
    public ArrayList<Integer> getGroupids() {
        return groupids;
    }

    public void setGroupids(ArrayList<Integer> groupids) {
        this.groupids = groupids;
    }

    public void removeAllGroups(){
        groups.clear();
        groupids.clear();
    }


    public ArrayList<String> getGroups(){return this.groups;}


}
