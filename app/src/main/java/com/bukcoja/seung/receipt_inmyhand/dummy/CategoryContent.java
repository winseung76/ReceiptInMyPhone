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
public class CategoryContent {

    /**
     * An array of sample (dummy) items.
     */
    public static  List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 2;

    static {

    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    public static DummyItem createDummyItem(int color,String category,int sum,int total,int ctgid) {
        return new DummyItem(color,category,sum,total,ctgid);
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
        private int color;
        private String category;
        private int ctgid;
        private int sum=0;
        private int total;


        public DummyItem(int color,String category,int sum,int total,int ctgid) {
            this.color=color;
            this.category = category;
            this.sum=sum;
            this.total=total;
            this.ctgid=ctgid;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCtgid() {
            return ctgid;
        }

        public void setCtgid(int ctgid) {
            this.ctgid = ctgid;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
