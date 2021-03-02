package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.List;

public class GroupListVO {

    private List<GroupVO> list;

    public int size(){return list.size();}

    public GroupVO get(int index){return list.get(index);}

}
