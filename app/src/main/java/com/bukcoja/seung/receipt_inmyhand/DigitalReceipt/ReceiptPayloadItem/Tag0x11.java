package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DataChanger;

import java.io.UnsupportedEncodingException;

import static com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DataChanger.hexStringTobyteArray;

public class Tag0x11 implements ReceiptItem{

    private static final byte TAG=0x11;
    private String membershipNum;
    private String kind;
    private short usable;
    private short total;
    private short nowUsed;
    private short nowSaved;
    //private String acceptMsg;

    public static final int MEMBERSHIPNUM_SIZE=8;
    //public static final int KIND_SIZE=10;   // 20 -> 10
    public static final int USABLE_SIZE=2;  // 4 -> 2
    public static final int TOTAL_SIZE=2;   // 4 -> 2
    public static final int NOWUSED_SIZE=2;  // 4-> 2
    public static final int NOWSAVED_SIZE=2;  // 4-> 2

    byte[] membershipNum_byte,usable_byte,total_byte,nowUsed_byte,nowSaved_byte;

    private int length=0;

    public Tag0x11(){

    }
    public Tag0x11(byte[] data){
        decode(data);
    }
    public String getMembershipNum() {
        return membershipNum;
    }

    public void setMembershipNum(String membershipNum) {
        this.membershipNum = membershipNum;
        membershipNum_byte=hexStringTobyteArray(membershipNum);
        length+=MEMBERSHIPNUM_SIZE;
        /*
        try {
            membershipNum_byte = membershipNum.getBytes("utf-8");
        }catch (UnsupportedEncodingException e){}
        length+=MEMBERSHIPNUM_SIZE;
        */
    }

    public String getKind() {
        return kind;
    }

    /*
    public void setKind(String kind) {
        this.kind = kind;
        try {
            kind_byte = kind.getBytes("utf-8");
        }catch (UnsupportedEncodingException e){}
        length+=KIND_SIZE;
    }
    */

    public int getUsable() {
        return usable;
    }

    public void setUsable(short usable) {
        this.usable = usable;
        usable_byte=DataChanger.shortToByteArray(usable);
        length+=USABLE_SIZE;
    }
    /*
    public void setUsable(int usable) {
        this.usable = usable;
        usable_byte=DataChanger.intToByteArray(usable);
        length+=USABLE_SIZE;
    }
    */

    public int getTotal() {
        return total;
    }

    public void setTotal(short total) {
        this.total = total;
        total_byte=DataChanger.shortToByteArray(total);
        length+=TOTAL_SIZE;
    }
    /*
    public void setTotal(int total) {
        this.total = total;
        total_byte=DataChanger.intToByteArray(total);
        length+=TOTAL_SIZE;
    }
    */

    public int getNowUsed() {
        return nowUsed;
    }

    public void setNowUsed(short nowUsed) {
        this.nowUsed = nowUsed;
        nowUsed_byte=DataChanger.shortToByteArray(nowUsed);
        length+=NOWUSED_SIZE;
    }
    /*
    public void setNowUsed(int nowUsed) {
        this.nowUsed = nowUsed;
        nowUsed_byte=DataChanger.intToByteArray(nowUsed);
        length+=NOWUSED_SIZE;
    }
    */

    public int getNowSaved() {
        return nowSaved;
    }

    public void setNowSaved(short nowSaved) {
        this.nowSaved = nowSaved;
        nowSaved_byte=DataChanger.shortToByteArray(nowSaved);
        length+=NOWSAVED_SIZE;
    }
    /*
    public void setNowSaved(int nowSaved) {
        this.nowSaved = nowSaved;
        nowSaved_byte=DataChanger.intToByteArray(nowSaved);
        length+=NOWSAVED_SIZE;
    }


    public String getAcceptMsg() {
        return acceptMsg;
    }

    public void setAcceptMsg(String acceptMsg) {
        this.acceptMsg = acceptMsg;
        acceptMsg_byte=acceptMsg.getBytes();
        length+=2+acceptMsg_byte.length;
    }
    */

    public byte[] encode(){

        //byte[] tlv=new byte[1+ Payload.LENGTH_SIZE+length];
        byte[] tlv=new byte[length];

        int index=0;

        System.arraycopy(membershipNum_byte,0,tlv,index,MEMBERSHIPNUM_SIZE);
        index+=MEMBERSHIPNUM_SIZE;

        /*
        System.arraycopy(kind_byte,0,tlv,index,kind_byte.length);
        index+=kind_byte.length;
        if(kind_byte.length<KIND_SIZE){
            int i=kind_byte.length;
            while(i<KIND_SIZE){
                tlv[index++]=(byte)0x00;
                i++;
            }
        }
        */

        System.arraycopy(usable_byte,0,tlv,index,USABLE_SIZE);
        index+=USABLE_SIZE;

        System.arraycopy(total_byte,0,tlv,index,TOTAL_SIZE);
        index+=TOTAL_SIZE;

        System.arraycopy(nowUsed_byte,0,tlv,index,NOWUSED_SIZE);
        index+=NOWUSED_SIZE;

        System.arraycopy(nowSaved_byte,0,tlv,index,NOWSAVED_SIZE);
        index+=NOWSAVED_SIZE;

        /*
        System.arraycopy(DataChanger.shortToByteArray((short)acceptMsg_byte.length),0,tlv,index,2);
        index+=2;
        System.arraycopy(acceptMsg_byte,0,tlv,index,acceptMsg_byte.length);
        index+=acceptMsg_byte.length;
        */

        return tlv;
    }


    public void decode(byte[] data)  {

        int index=0;
        byte[] temp;


        try {
            // membershipNum 추출
            temp=new byte[MEMBERSHIPNUM_SIZE];
            System.arraycopy(data,index,temp,0,MEMBERSHIPNUM_SIZE);
            membershipNum=new String(temp,"utf-8");
            System.out.println("membershipNum : "+membershipNum);
            index+=MEMBERSHIPNUM_SIZE;

            /*
            // kind 추출
            temp=new byte[KIND_SIZE];
            System.arraycopy(data,index,temp,0,KIND_SIZE);
            int i=0;
            while(temp[i]!=(byte)0x00){
                i++;
            }
            byte[] result=new byte[i];

            for(int k=0;k<i;k++){
                result[k]=temp[k];
            }

            kind=new String(result,"utf-8");
            System.out.println("kind : "+kind);
            index+=KIND_SIZE;
            */

        }catch (UnsupportedEncodingException e){}

        // usable 추출
        temp=new byte[USABLE_SIZE];
        System.arraycopy(data,index,temp,0,USABLE_SIZE);
        usable=DataChanger.byteArrayToShort(temp);
        System.out.println("usable : "+usable);
        index+=USABLE_SIZE;

        // total 추출
        temp=new byte[TOTAL_SIZE];
        System.arraycopy(data,index,temp,0,TOTAL_SIZE);
        total=DataChanger.byteArrayToShort(temp);
        System.out.println("total : "+total);
        index+=TOTAL_SIZE;

        // nowused 추출
        temp=new byte[NOWUSED_SIZE];
        System.arraycopy(data,index,temp,0,NOWUSED_SIZE);
        nowUsed=DataChanger.byteArrayToShort(temp);
        System.out.println("nowUsed : "+nowUsed);
        index+=NOWUSED_SIZE;

        // nowsaved 추출
        temp=new byte[NOWSAVED_SIZE];
        System.arraycopy(data,index,temp,0,NOWSAVED_SIZE);
        nowSaved=DataChanger.byteArrayToShort(temp);
        System.out.println("nowSaved : "+nowSaved);
        index+=NOWSAVED_SIZE;

        try {
            // acceptMsg 추출
            temp=new byte[2];
            System.arraycopy(data,index,temp,0,2);
            length=DataChanger.byteArrayToShort(temp);
            temp=new byte[length];
            index+=2;
            System.arraycopy(data,index,temp,0,length);
            //acceptMsg=new String(temp, "utf-8");
            //System.out.println("acceptNsg : " + acceptMsg);
            index+=length;

        }catch (Exception e){}


    }

    public int getLength(){
        return length;
    }

    public byte getTag(){
        return TAG;
    }

}
