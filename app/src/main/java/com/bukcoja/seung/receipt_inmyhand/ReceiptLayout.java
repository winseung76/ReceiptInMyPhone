package com.bukcoja.seung.receipt_inmyhand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ReceiptLayout {

    private View dialogView;
    private LayoutInflater inflater;
    private TextView cn, crn, name, address,tel,tob, date,price,vat,mop,mods,item_num;
    private TextView savedPoint,usablePoint,totalPoint,usedPoint;
    private TextView exceiptvat;
    private TextView userName;
    private TextView sum;
    private HashMap<String,String> map;
    private HashMap<String,ArrayList<String>> prods;
    private LinearLayout listLayout;
    private ReceiptVO.Receipt receipt;
    DecimalFormat formatter=new DecimalFormat("###,###");


    public ReceiptLayout(Context context,ReceiptVO.Receipt receipt){
        this.receipt=receipt;

        inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.receipt_dialog, null);
        listLayout=dialogView.findViewById(R.id.listLayout);

        userName=dialogView.findViewById(R.id.userName);
        cn=dialogView.findViewById(R.id.cn);
        crn=dialogView.findViewById(R.id.crn);
        name=dialogView.findViewById(R.id.name);
        address=dialogView.findViewById(R.id.address);
        //tel=dialogView.findViewById(R.id.tel);
        date=dialogView.findViewById(R.id.date);
        price=dialogView.findViewById(R.id.price);
        vat=dialogView.findViewById(R.id.vat);
        exceiptvat=dialogView.findViewById(R.id.exceiptvat);
        savedPoint=dialogView.findViewById(R.id.savedPoint);
        usablePoint=dialogView.findViewById(R.id.usablePoint);
        totalPoint=dialogView.findViewById(R.id.totalPoint);
        usedPoint=dialogView.findViewById(R.id.usedPoint);
        sum=dialogView.findViewById(R.id.sum);

        userName=dialogView.findViewById(R.id.userName);
        exceiptvat=dialogView.findViewById(R.id.exceiptvat);

    }
    public void insertInfo(){

        /*
        cn.setText(map.get("cn"));
        String crnstr=map.get("crn");
        crn.setText(crnstr.substring(0,3)+"-"+crnstr.substring(3,5)+"-"+crnstr.substring(5,10));
        name.setText(map.get("name"));
        address.setText(map.get("address"));
        //tel.setText(map.get("tel"));
        */

        cn.setText(receipt.getCn());
        //String crnstr=map.get("crn");
        String regnum=receipt.getRegnum();

        crn.setText(regnum.substring(0,3)+"-"+regnum.substring(3,5)+"-"+regnum.substring(5));
        name.setText(receipt.getRepresenter());
        address.setText(receipt.getAddress());
        //tel.setText(map.get("tel"));
        date.setText(receipt.getDate());

        price.setText(formatter.format(receipt.getTotalprice()));

        // 총액으로부터 부가가치세 구하기
        double vatprice=receipt.getTotalprice()*0.1;
        vat.setText(formatter.format(vatprice));

        savedPoint.setText(formatter.format(receipt.getSavedpoint()));
        usablePoint.setText(formatter.format(receipt.getUsablepoint()));
        totalPoint.setText(formatter.format(receipt.getTotalpoint()));
        usedPoint.setText(formatter.format(receipt.getUsedpoint()));
        sum.setText(formatter.format(receipt.getTotalprice()+receipt.getUsedpoint()));

        userName.setText(receipt.getUserid());
        // 총액에서 부가가치세를 뺀 금액이 부가세 과세 물품가액
        exceiptvat.setText(formatter.format(receipt.getTotalprice()-vatprice));
        /*
        price.setText(formatter.format(Integer.parseInt(map.get("price"))));
        vat.setText(formatter.format(Integer.parseInt(map.get("vat"))));
        int dif=Integer.parseInt(map.get("price"))-Integer.parseInt(map.get("vat"));
        exceiptvat.setText(formatter.format(dif));
        userName.setText(map.get("username"));

        savedPoint.setText(formatter.format(Integer.parseInt(map.get("nowSaved"))));
        usablePoint.setText(formatter.format(Integer.parseInt(map.get("usable"))));
        totalPoint.setText(formatter.format(Integer.parseInt(map.get("total"))));
        usedPoint.setText(formatter.format(Integer.parseInt(map.get("nowUsed"))));
        sum.setText(formatter.format(Integer.parseInt(map.get("price"))+Integer.parseInt(map.get("nowUsed"))));
        */

        listLayout.setVisibility(View.VISIBLE);

        insertPurchaseLists();
    }

    public void insertPurchaseLists(){

        /*
        TextView pName=layout.findViewById(R.id.pName);
        TextView unitPrice=layout.findViewById(R.id.unitPrice);
        TextView num=layout.findViewById(R.id.number);
        TextView result=layout.findViewById(R.id.resultPrice);
        */
        TextView pName,unitPrice,num,result;

        int size=receipt.getProducts().size();
        for(int i=0;i<size;i++){
            ReceiptVO.Product product=receipt.getProducts().get(i);

            LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.purchaselist,null);
            listLayout.addView(layout);
            pName=layout.findViewById(R.id.pName);
            unitPrice=layout.findViewById(R.id.unitPrice);
            num=layout.findViewById(R.id.number);
            result=layout.findViewById(R.id.resultPrice);

            pName.setText((i+1)+". " +product.getProductname());
            unitPrice.setText(formatter.format(product.getUnitprice()));
            num.setText(String.valueOf(product.getCount()));
            result.setText(formatter.format(product.getUnitprice()*product.getCount()));
        }
        /*
        int size=prods.get("pName").size();


        Log.d("ReceiptLayout",String.valueOf(size));

        for(int i=0;i<size;i++){
            LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.purchaselist,null);
            listLayout.addView(layout);
            pName=layout.findViewById(R.id.pName);
            unitPrice=layout.findViewById(R.id.unitPrice);
            num=layout.findViewById(R.id.number);
            result=layout.findViewById(R.id.resultPrice);

            pName.setText((i+1)+". " +prods.get("pName").get(i));
            unitPrice.setText(formatter.format(Integer.parseInt(prods.get("unitPrice").get(i))));
            num.setText(prods.get("num").get(i));
            result.setText(formatter.format(Integer.parseInt(prods.get("unitPrice").get(i))*Integer.parseInt(prods.get("num").get(i))));
        }
        */

        /*
        pName.setText("1. "+prods.get("pName").get(0));
        unitPrice.setText(formatter.format(Integer.parseInt(prods.get("unitPrice").get(0))));
        num.setText(prods.get("num").get(0));
        result.setText(formatter.format(Integer.parseInt(prods.get("unitPrice").get(0))*Integer.parseInt(prods.get("num").get(0))));
        */
    }
    public View getReceipt(){
        return dialogView;
    }
}
