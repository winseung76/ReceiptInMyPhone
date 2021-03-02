package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DataChanger;

import java.util.ArrayList;

public class Tag0x21 implements ReceiptItem{

    private final byte TAG=0x21;
    private int length=5;
    private ArrayList<Product> products=new ArrayList<>();
    //private ArrayList<byte[]> others=new ArrayList<>();

    public static final int PNAME_SIZE=20;
    public static final int PINFO_SIZE=1;
    public static final int UNITPRICE_SIZE=2;
    public static final int CODE_SIZE=4;
    public static final int NUM_SIZE=1;

    public Tag0x21(){

    }
    public Tag0x21(byte[] data){
        decode(data);
    }

    //public ArrayList<String> getpName() { return pName; }

    public void addProduct(Product product){
        this.products.add(product);
    }

    /*
    public ArrayList<Byte> getpInfo() {
        return pInfo;
    }

    public ArrayList<Short> getUnitPrice() {
        return unitPrice;
    }

    public ArrayList<Byte> getNum() {
        return num;
    }
    */

    /*
    public ArrayList<byte[]> getOthers() {
        return others;
    }

    public void setOthers(int code,String similarGroup,int num) {
        byte[] others=new byte[OTHERS_SIZE];
        int index=0;

        others[index++]=(byte)code;

        int size=similarGroup.length()/2;
        System.arraycopy(DataChanger.hexStringTobyteArray(similarGroup),0,others,1,size);
        index+=size;

        if(size==2)
            others[index]=(byte)num;

        //this.others = others;
        this.others.add(others);
        //length+=OTHERS_SIZE;
    }
    */

    // 코드로 안할 때의 인코딩 메서드
    /*
    public byte[] encode(){

        //System.out.println("인코딩 전 prdoNum : "+prodNum);
        int prodNum=products.size();
        byte[] tlv=new byte[length*prodNum];
        int index=0;

        try {
            for (int k = 0; k < prodNum; k++) {

                Product p=products.get(k);

                //System.out.println("K : "+k);
                //System.arraycopy(pName_byte, 0, tlv, index, pName_byte.length);
                //byte[] pNameBytes=pName.get(k).getBytes("utf-8");
                byte[] pNameBytes=p.getName().getBytes("utf-8");
                int pNameLength=pNameBytes.length;

                //System.arraycopy(pNameBytes, 0, tlv, index, pNameLength);
                //index += pNameLength;
                if (pNameLength < PNAME_SIZE) {
                    System.arraycopy(pNameBytes, 0, tlv, index, pNameLength);
                    index += pNameLength;

                    int i = pNameLength;
                    while (i < PNAME_SIZE) {
                        tlv[index++] = (byte) 0x00;
                        i++;
                    }
                }

                //tlv[index++] = pInfo.get(k);
                tlv[index++] = p.getInfo();

                //System.arraycopy(DataChanger.shortToByteArray(unitPrice.get(k)), 0, tlv, index, UNITPRICE_SIZE);
                System.arraycopy(DataChanger.shortToByteArray(p.getUnitPrice()), 0, tlv, index, UNITPRICE_SIZE);
                index += UNITPRICE_SIZE;

                tlv[index++] = p.getNum();
            }
        }catch (UnsupportedEncodingException e){
        }
        return tlv;
    }
    */
    public byte[] encode(){

        int prodNum=products.size();
        byte[] tlv=new byte[length*prodNum];
        int index=0;

        try {
            for (int k = 0; k < prodNum; k++) {

                Product p=products.get(k);

                int code=p.getCode();
                System.arraycopy(DataChanger.intToByteArray(code), 0, tlv, index, CODE_SIZE);
                index += CODE_SIZE;

                tlv[index++] = p.getNum();
            }

        }catch (Exception e){
        }
        return tlv;
    }

    /*
    public void decode(byte[] data)  {

        int index=0;
        byte[] temp;

        System.out.println("data size : "+data.length);
        int prodNum=data.length/length;
        System.out.println("prodNum : "+prodNum);
        for(int j=0;j<prodNum;j++){

            try {
                // pName 추출
                temp=new byte[PNAME_SIZE];
                System.arraycopy(data,index,temp,0,PNAME_SIZE);
                int i=0;
                while(temp[i]!=(byte)0x00){
                    i++;
                }
                byte[] result=new byte[i];

                for(int k=0;k<i;k++){
                    result[k]=temp[k];
                }

                String name=new String(result,"utf-8");
                //pName.add(new String(result,"utf-8"));
                System.out.println("pName : "+name);
                index+=PNAME_SIZE;

                // pInfo 추출
                byte pinfo=data[index++];
                //pInfo.add(pinfo);
                System.out.println("pInfo : "+pinfo);

                // unitPrice 추출
                temp=new byte[UNITPRICE_SIZE];
                System.arraycopy(data,index,temp,0,UNITPRICE_SIZE);
                short unitprice=DataChanger.byteArrayToShort(temp);
                //short unitprice=ByteBuffer.wrap(temp, 0, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
                //unitPrice.add(unitprice);
                System.out.println("unitPrice : "+unitprice);
                index+=UNITPRICE_SIZE;

                // unitPrice 추출
                byte num=data[index++];
                //num.add(numbyte);
                System.out.println("num : "+num);

                products.add(new Product(name,num,unitprice,pinfo));

                // others 추출

               // temp=new byte[OTHERS_SIZE];
                //System.arraycopy(data,index,temp,0,OTHERS_SIZE);
                //others.add(temp);
               // System.out.println("others : "+temp);
               // index+=OTHERS_SIZE;


            }catch (Exception e){e.printStackTrace();}
        }
    }
    */
    public void decode(byte[] data)  {

        int index=0;
        byte[] temp;

        System.out.println("data size : "+data.length);
        int prodNum=data.length/length;
        System.out.println("prodNum : "+prodNum);

        for(int j=0;j<prodNum;j++){

            try {

                // code 추출
                temp=new byte[CODE_SIZE];
                System.arraycopy(data,index,temp,0,CODE_SIZE);
                int code=DataChanger.byteArrayToInt(temp);
                System.out.println("code : "+code);
                index+=CODE_SIZE;

                // num 추출
                byte num=data[index++];
                //num.add(numbyte);
                System.out.println("num : "+num);

                products.add(new Product(code,num));

            }catch (Exception e){e.printStackTrace();}
        }
    }
    public Product getProduct(int index){ return products.get(index);}
    public int getProdNum(){return products.size();}
    public int getLength(){
        return length*products.size();
    }
    public byte getTag(){
        return TAG;
    }

}
