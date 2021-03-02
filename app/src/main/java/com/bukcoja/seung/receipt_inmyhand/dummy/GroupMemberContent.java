package com.bukcoja.seung.receipt_inmyhand.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMemberContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<GroupMemberContent.DummyItem> ITEMS = new ArrayList<GroupMemberContent.DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, GroupMemberContent.DummyItem> ITEM_MAP = new HashMap<String,GroupMemberContent.DummyItem>();

    private static final int COUNT = 25;

    static {

    }

    public static void addItem(GroupMemberContent.DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.userid, item);
    }

    public static GroupMemberContent.DummyItem createDummyItem(String imageUri,String userid) {
        return new GroupMemberContent.DummyItem(imageUri,userid);
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

        public final String userid;
        public final String imageUri;

        public DummyItem(String imageUri, String userid) {
            this.imageUri=imageUri;
            this.userid=userid;
        }

        @Override
        public String toString() {
            return userid;
        }
    }
}
