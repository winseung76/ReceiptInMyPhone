package com.bukcoja.seung.receipt_inmyhand.VO;

public class ProductVO {

    private String prodname;
    private int unitprice;
    private int count;
    private int ctgid;

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCtgid() {
        return ctgid;
    }

    public void setCtgid(int ctgid) {
        this.ctgid = ctgid;
    }
}
