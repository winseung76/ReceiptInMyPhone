package com.bukcoja.seung.receipt_inmyhand.VO;

public class ServiceVO {

    private RcmdStore rank1;
    private RcmdStore rank2;
    private RcmdStore rank3;

    public RcmdStore getRank1() {
        return rank1;
    }

    public void setRank1(RcmdStore rank1) {
        this.rank1 = rank1;
    }

    public RcmdStore getRank2() {
        return rank2;
    }

    public void setRank2(RcmdStore rank2) {
        this.rank2 = rank2;
    }

    public RcmdStore getRank3() {
        return rank3;
    }

    public void setRank3(RcmdStore rank3) {
        this.rank3 = rank3;
    }

    public class RcmdStore{

        private String store;
        private String address;
        private String menu;
        private int count;
        private int time;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMenu() {
            return menu;
        }

        public void setMenu(String menu) {
            this.menu = menu;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
