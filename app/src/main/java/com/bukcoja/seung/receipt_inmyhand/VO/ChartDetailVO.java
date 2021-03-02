package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.ArrayList;

public class ChartDetailVO {

    private ArrayList<ProdPerDate> list;
    //public String res;

    public int size(){return list.size();}

    public ProdPerDate get(int index){return list.get(index);}


    public class ProdPerDate{

        private String date;
        private int totalprice;
        private ArrayList<ProductVO> products;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(int totalprice) {
            this.totalprice = totalprice;
        }

        public ArrayList<ProductVO> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<ProductVO> products) {
            this.products = products;
        }
    }

}
