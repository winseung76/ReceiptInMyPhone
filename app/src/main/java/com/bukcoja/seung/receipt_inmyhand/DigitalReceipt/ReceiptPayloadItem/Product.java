package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem;
/* 상품 정보를 가지고 있는 클래스 */
public class Product {

    private String name;
    private short unitPrice;
    private byte num;
    private byte info;
    private int code;

    public Product(String name,byte num,short unitPrice,byte info){
        this.name=name;
        this.unitPrice=unitPrice;
        this.num=num;
        this.info=info;
    }

    public Product(int code,byte num){
        this.code=code;
        this.num=num;
    }


    public String getName() {
        return name;
    }

    public short getUnitPrice() {
        return unitPrice;
    }

    public byte getNum() {
        return num;
    }

    public byte getInfo() {
        return info;
    }

    public int getCode(){return code;}
}
