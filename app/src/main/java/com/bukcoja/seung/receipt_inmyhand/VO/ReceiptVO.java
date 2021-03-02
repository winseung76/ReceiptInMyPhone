package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.ArrayList;
import java.util.List;

public class ReceiptVO {

    private List<Receipt> list;

    public ReceiptVO(){
        list=new ArrayList<>();
    }
    public List<Receipt> getList() {
        return list;
    }

    public void setList(List<Receipt> list) {
        this.list = list;
    }

    public int size(){return list.size();}

    public void add(Receipt receipt){
        list.add(receipt);
    }
    public Receipt get(int i){return list.get(i);}

    public Receipt createReceipt(){
        return new Receipt();
    }

    public class Receipt{
        private String userid;
        private int receiptid;
        private int totalprice;
        private String date;
        private int usedpoint;
        private int usablepoint;
        private int savedpoint;
        private int totalpoint;

        private String cn;
        private String representer;
        private String address;
        private String regnum;


        private List<Product> products;

        public Receipt(){
            products=new ArrayList<>();
        }

        public void add(Product product){
            products.add(product);
        }
        public Product createProduct(){
            return new Product();
        }
        public int getReceiptid() {
            return receiptid;
        }

        public void setReceiptid(int receiptid) {
            this.receiptid = receiptid;
        }

        public int getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(int totalprice) {
            this.totalprice = totalprice;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getUsedpoint() {
            return usedpoint;
        }

        public void setUsedpoint(int usedpoint) {
            this.usedpoint = usedpoint;
        }

        public int getUsablepoint() {
            return usablepoint;
        }

        public void setUsablepoint(int usablepoint) {
            this.usablepoint = usablepoint;
        }

        public int getSavedpoint() {
            return savedpoint;
        }

        public void setSavedpoint(int savedpoint) {
            this.savedpoint = savedpoint;
        }

        public int getTotalpoint() {
            return totalpoint;
        }

        public void setTotalpoint(int totalpoint) {
            this.totalpoint = totalpoint;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCn() {
            return cn;
        }

        public void setCn(String cn) {
            this.cn = cn;
        }

        public String getRepresenter() {
            return representer;
        }

        public void setRepresenter(String representer) {
            this.representer = representer;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRegnum() {
            return regnum;
        }

        public void setRegnum(String regnum) {
            this.regnum = regnum;
        }
    }
    public class Product{
        private String productname;
        private int unitprice;
        private int count;

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
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
    }

}
