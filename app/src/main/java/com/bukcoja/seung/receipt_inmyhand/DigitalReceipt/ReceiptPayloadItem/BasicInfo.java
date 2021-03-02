package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DataChanger;

import static com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DataChanger.hexStringTobyteArray;

public class BasicInfo implements ReceiptItem{

    private final byte TAG=0x01;
    private int length=0;

    private static final int TOI_SIZE=1;     // type of issue 발행 종류
    private static final int CRN_SIZE=5;     // company register number : 사업자 등록 번호
    //private static final int TEL_SIZE=12;    // 연락처
    private static final int TOB_SIZE=1;     // 4 -> 1  // type of business : 업종
    private static final int DATE_SIZE=6;    // 전자 영수증 발행 일시
    private static final int PRICE_SIZE=4;   // 거래 총액
    private static final int VAT_SIZE=2;     // 4->2   // 부가가치세
    //private static final int MOBS_SIZE=1;
    private static final int ITEMNUM_SIZE=1; // 상품/서비스 항목 수

    private byte toi;
    private String crn;
    private String cn;
    private String name;
    private String address;
    private String tel;
    private byte tob;
    private String date;
    private int price;
    private short vat;
    //private byte mop;
    //private byte mobs;
    private byte item_num;

    byte[] crn_byte,cn_byte,name_byte,address_byte,tel_byte,date_byte,price_byte,vat_byte;
    //byte[] tob_byte;


    public BasicInfo(){

    }
    public BasicInfo(byte[] data){
        //System.out.println("data"+(data==null ? "Yes":"No"));
        decode(data);
    }

    public byte getToi() { return toi; }

    public void setToi(byte toi) {
        this.toi = toi;
        length+=TOI_SIZE;
    }

    public String getCrn() { return crn; }

    public void setCrn(String crn) {
        this.crn = crn;
        crn_byte=hexStringTobyteArray(crn);
        length+=CRN_SIZE;
    }

    public String getCn() { return cn; }

    public void setCn(String cn) {
        this.cn = cn;
        cn_byte=cn.getBytes();
        length+=2+cn_byte.length;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        name_byte=name.getBytes();
        length+=2+name_byte.length;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
        address_byte=address.getBytes();
        length+=2+address_byte.length;
    }

    public String getTel() { return tel; }

    /*
    public void setTel(String tel) {
        this.tel = tel;
        try {
            tel_byte = tel.getBytes("utf-8");
        }catch (UnsupportedEncodingException e){}
        length+=TEL_SIZE;
    }
    */

    public int getTob() { return tob; }

    public void setTob(byte tob) {
        this.tob = tob;
        length+=TOB_SIZE;
    }
    /*
    public void setTob(int tob) {
        this.tob = tob;
        tob_byte=intToByteArray(tob);
        length+=TOB_SIZE;
    }
    */

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date=date.substring(2,4)+date.substring(5,7)+date.substring(8,10)+date.substring(11,13)+
                date.substring(14,16)+date.substring(17,19);
        date_byte=hexStringTobyteArray(this.date);
        length+=DATE_SIZE;
    }

    public int getPrice() { return price; }


    public void setPrice(int price) {
        this.price = price;
        price_byte= DataChanger.intToByteArray(price);
        length+=PRICE_SIZE;
    }


    public int getVat() { return vat; }

    public void setVat(short vat) {
        this.vat = vat;
        vat_byte=DataChanger.shortToByteArray(vat);
        length+=VAT_SIZE;
    }
    /*
    public void setVat(int vat) {
        this.vat = vat;
        vat_byte=intToByteArray(vat);
        length+=VAT_SIZE;
    }
    */

    /*
    public byte getMop() { return mop; }

    public void setMop(byte mop) {
        this.mop = mop;
        length+=MOP_SIZE;
    }


    public byte getMobs() { return mobs; }

    public void setMobs(byte mobs) {
        this.mobs = mobs;
        length+=MOBS_SIZE;
    }
    */

    public byte getItem_num() { return item_num; }

    public void setItem_num(byte item_num) {
        this.item_num = item_num;
        length+=ITEMNUM_SIZE;
    }

    public byte[] encode(){

        //byte[] tlv=new byte[Payload.TAG_SIZE+ Payload.LENGTH_SIZE+length];
        byte[] tlv=new byte[length];

        //tlv[0]=TAG;
        //System.arraycopy(DataChanger.intToByteArray(length),0,tlv,1,LENGTH_SIZE);
        int index=0;

        tlv[index++]=toi;

        /*
        System.arraycopy(crn_byte,0,tlv,index,CRN_SIZE);
        index+=CRN_SIZE;

        System.arraycopy(DataChanger.shortToByteArray((short)cn_byte.length),0,tlv,index,2);
        index+=2;// cn은 가변 길이이므로 길이 정보 필요
        System.arraycopy(cn_byte,0,tlv,index,cn_byte.length);
        index+=cn_byte.length;

        Log.e("company name",String.valueOf(cn_byte.length));

        System.arraycopy(DataChanger.shortToByteArray((short)name_byte.length),0,tlv,index,2);
        index+=2; // name은 가변 길이이므로 길이 정보 필요
        System.arraycopy(name_byte,0,tlv,index,name_byte.length);
        index+=name_byte.length;

        Log.e("representer",String.valueOf(name_byte.length));

        System.arraycopy(DataChanger.shortToByteArray((short)address_byte.length),0,tlv,index,2);
        index+=2;// address은 가변 길이이므로 길이 정보 필요
        System.arraycopy(address_byte,0,tlv,index,address_byte.length);
        index+=address_byte.length;
        Log.e("address",String.valueOf(address_byte.length));
        */
        /*
        System.arraycopy(tel_byte,0,tlv,index,tel_byte.length);
        index+=tel_byte.length;
        if(tel_byte.length<TEL_SIZE){
            int i=tel_byte.length;
            while(i<TEL_SIZE){
                tlv[index++]=(byte)0x00;
                i++;
            }
        }
        */

        //System.arraycopy(tob,0,tlv,index,TOB_SIZE);
        tlv[index++]=tob;
        //index+=TOB_SIZE;

        System.arraycopy(date_byte,0,tlv,index,DATE_SIZE);
        index+=DATE_SIZE;

        System.arraycopy(price_byte,0,tlv,index,PRICE_SIZE);
        index+=PRICE_SIZE;

        System.arraycopy(vat_byte,0,tlv,index,VAT_SIZE);
        index+=VAT_SIZE;

        //tlv[index++]=mop;
        //tlv[index++]=mobs;
        tlv[index]=item_num;

        return tlv;
    }


    public void decode(byte[] data)  {

        int index=0;

        // 태그 추출
        //tag=data[index++];
        //System.out.println("tag : "+TAG);

        // 길이 추출
        byte[] temp;
        //System.arraycopy(data,index,temp,0,LENGTH_SIZE);
        //int l=DataChanger.byteArrayToInt(temp);
        //System.out.println("length : "+l);
        //index+=4;
        index=0;

        //System.out.println("데이터 사이즈 : "+data.length);
        // toi 추출
        System.out.println("toi : "+data[index++]);
        //index+=TOI_SIZE;

        /*
        // crn 추출
        temp=new byte[CRN_SIZE];
        System.arraycopy(data,index,temp,0,CRN_SIZE);
        crn=DataChanger.byteArrayToHexString(temp);
        System.out.println("crn : "+crn);
        index+=CRN_SIZE;
        */

        try {
            /*
            // cn 추출
            temp=new byte[2];
            System.arraycopy(data,index,temp,0,2);
            short length=DataChanger.byteArrayToShort(temp);
            //System.out.println(length);
            temp=new byte[length];
            index+=2;
            System.arraycopy(data,index,temp,0,length);
            cn= new String(temp, "utf-8");
            System.out.println("cn : " + cn);
            index+=length;

            // name 추출
            temp=new byte[2];
            System.arraycopy(data,index,temp,0,2);
            length=DataChanger.byteArrayToShort(temp);
            temp=new byte[length];
            index+=2;
            System.arraycopy(data,index,temp,0,length);
            name=new String(temp, "utf-8");
            System.out.println("name : " + name);
            index+=length;

            // address 추출
            temp=new byte[2];
            System.arraycopy(data,index,temp,0,2);
            length=DataChanger.byteArrayToShort(temp);
            temp=new byte[length];
            index+=2;
            System.arraycopy(data,index,temp,0,length);
            address=new String(temp, "utf-8");
            System.out.println("address : " + address);
            index+=length;
            */

            /*
            // tel 추출
            temp=new byte[11];
            System.arraycopy(data,index,temp,0,11);
            tel=new String(temp,"utf-8");
            System.out.println("tel : "+tel);
            index+=TEL_SIZE;
            */

        }catch (Exception e){}

        // tob 추출
        tob=data[index++];
        System.out.println("tob : "+tob);
        /*
        temp=new byte[TOB_SIZE];
        System.arraycopy(data,index,temp,0,TOB_SIZE);
        tob=DataChanger.byteArrayToInt(temp);
        System.out.println("tob : "+tob);
        index+=TOB_SIZE;
        */

        // date 추출
        temp=new byte[DATE_SIZE];
        System.arraycopy(data,index,temp,0,DATE_SIZE);
        date=DataChanger.byteArrayToHexString(temp);
        date="20"+date.substring(0,2)+"-"+date.substring(2,4)+"-"+date.substring(4,6)+" "+
                date.substring(6,8)+":"+date.substring(8,10)+":"+date.substring(10,12);
        System.out.println("date : "+date);
        index+=DATE_SIZE;

        // price 추출
        temp=new byte[PRICE_SIZE];
        System.arraycopy(data,index,temp,0,PRICE_SIZE);
        price=DataChanger.byteArrayToInt(temp);
        System.out.println("price : "+price);
        index+=PRICE_SIZE;
        //System.out.println("남은 크기1 : "+index);

        // vat 추출
        temp=new byte[VAT_SIZE];
        System.arraycopy(data,index,temp,0,VAT_SIZE);
        vat=DataChanger.byteArrayToShort(temp);
        System.out.println("vat : "+vat);
        index+=VAT_SIZE;
        //System.out.println("남은 크기2 : "+index);

        // mop 추출
        //mop=data[index++];
        //System.out.println("mop : "+mop);

        // mobs 추출
        //mobs=data[index++];
        //System.out.println("mobs : "+mobs);

        // itemNum 추출
        item_num=data[index];
        System.out.println("item_num : "+item_num);

    }

    public int getLength(){ return length;}
    public byte getTag(){
        return TAG;
    }

}
