package com.bukcoja.seung.receipt_inmyhand.ListVO;

public class ListVO {

    private String group;
    private int groupid;

    public void setGroup(int id){
        this.groupid=id;
    }
    public int getGroupid(){
        return groupid;
    }

    public void setGroupName(String name){
        this.group=name;
    }
    public String getGroupName(){
        return group;
    }
}
