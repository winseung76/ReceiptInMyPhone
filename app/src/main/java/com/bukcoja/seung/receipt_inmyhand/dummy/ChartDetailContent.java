package com.bukcoja.seung.receipt_inmyhand.dummy;

import com.bukcoja.seung.receipt_inmyhand.VO.ProductVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartDetailContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<ChartDetailContent.DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ChartDetailContent.DummyItem> ITEM_MAP = new HashMap<String,ChartDetailContent.DummyItem>();

    private static final int COUNT = 25;

    static {

    }

    public static void addItem(ChartDetailContent.DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.productname, item);
    }

    public static ChartDetailContent.DummyItem createDummyItem(ProductVO productVO) {
        return new ChartDetailContent.DummyItem(productVO);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class DummyItem {
        //public final String id;
        public final String productname;
        public final int unitprice;
        public final int count;

        public DummyItem(ProductVO productVO) {
            //this.id = id;
            this.productname=productVO.getProdname();
            this.unitprice=productVO.getUnitprice();
            this.count=productVO.getCount();
        }

        @Override
        public String toString() {
            return productname;
        }
    }
}
