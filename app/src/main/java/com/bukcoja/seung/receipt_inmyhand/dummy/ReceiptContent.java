package com.bukcoja.seung.receipt_inmyhand.dummy;

import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ReceiptContent {

    /**
     * An array of sample (dummy) items.
     */
    public static  List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 1;

    static {
        // Add some sample items.
        /*
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(0,"스타벅스","양천구 목동동로 12길",5000,"식음료",2019,02,22));
        }
        */
        //addItem(createDummyItem(0,"스타벅스","양천구 목동동로 12길",5000,2019,02,22));
        //addItem(createDummyItem(0,"스타벅스","양천구 목동동로 12길",5000,2019,02,22));
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    /*
    public static DummyItem createDummyItem(int id,HashMap<String,String> map,HashMap<String,ArrayList<String>> prods) {
        return new DummyItem(id,map,prods);
    }
    */
    public static DummyItem createDummyItem(ReceiptVO.Receipt receipt) {
        return new DummyItem(receipt);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    // 하나의 리스트에 들어갈 내용들을 담은 클래스
    public static class DummyItem {
        private String name;
        private int price;
        private String address=null;
        private int id;
        private HashMap<String,String> map;
        private HashMap<String,ArrayList<String>> prods;
        private ReceiptVO.Receipt receipt;

        public DummyItem(int id,HashMap<String,String> map,HashMap<String,ArrayList<String>> prods) {
            this.id=id;
            this.address=map.get("address");
            this.name = map.get("name");
            this.price = Integer.parseInt(map.get("price"));
            this.map=map;
            this.prods=prods;
        }

        public DummyItem(ReceiptVO.Receipt receipt){
            this.receipt=receipt;
            this.id=receipt.getReceiptid();
        }

        public ReceiptVO.Receipt getReceipt(){return receipt;}

        public int getId() { return id; }

        public void setId(int id) { this.id = id; }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public HashMap<String, String> getMap() {
            return map;
        }

        public void setMap(HashMap<String, String> map) {
            this.map = map;
        }


        public HashMap<String, ArrayList<String>> getProds() {
            return prods;
        }

        public void setProds(HashMap<String, ArrayList<String>> prods) {
            this.prods = prods;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
