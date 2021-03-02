package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.List;

public class GroupMemberListVO {

    private List<GroupMemberVO> list;

    public int size(){return list.size(); }

    public GroupMemberVO get(int index){return list.get(index);}

    public class GroupMemberVO{

        private String userid;
        private String imageuri;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getImageUri() {
            return imageuri;
        }

        public void setImageUri(String imageUri) {
            this.imageuri = imageUri;
        }
    }
}
