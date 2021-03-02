package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.List;

public class ChartListVO {

    private List<ChartVO> list;
    private int totalprice;

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public List<ChartVO> getList() {
        return list;
    }

    public void setList(List<ChartVO> list) {
        this.list = list;
    }

    public int size(){return list.size();}

    public ChartVO get(int index){return list.get(index);}


}
