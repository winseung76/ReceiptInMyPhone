package com.bukcoja.seung.receipt_inmyhand.dummy;


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
public class PointSavingContent {

    /**
     * An array of sample (dummy) items.
     */
    public List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 2;

    static {

    }

    public void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    public static DummyItem createDummyItem(String date,int state,int point,int total) {
        return new DummyItem(date,state,point,total);
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
        private int id;
        private String date;
        private int state;  // 적립이면 0, 사용이면 1
        private int point;
        private int total;


        public DummyItem(String date,int state,int point,int total) {
            this.date=date;
            this.state=state;
            this.point=point;
            this.total=total;
        }

        public String getDate() { return date; }

        public void setDate(String date) { this.date = date; }

        public int getState() { return state; }

        public void setState(int state) { this.state = state; }

        public int getPoint(){return this.point;}

        public void setPoint(int point){this.point=point;}

        public int getTotal(){return total;}

        @Override
        public String toString() {
            return "";
        }
    }
}
