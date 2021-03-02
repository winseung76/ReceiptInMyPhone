package com.bukcoja.seung.receipt_inmyhand.dummy;

import android.graphics.drawable.Drawable;

import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;

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
public class LunchContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {

    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.store, item);
    }

    public static DummyItem createDummyItem(Drawable image, ServiceVO.RcmdStore store) {
        return new DummyItem(image,store);
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
    public static class DummyItem {
        public final String store;
        public final String address;
        public final Drawable image;
        public final String menu;
        public final int count;

        public DummyItem(Drawable image, ServiceVO.RcmdStore store) {
            this.image=image;
            this.store=store.getStore();
            this.address=store.getAddress();
            this.menu=store.getMenu();
            this.count=store.getCount();
        }

        @Override
        public String toString() {
            return store;
        }
    }
}
