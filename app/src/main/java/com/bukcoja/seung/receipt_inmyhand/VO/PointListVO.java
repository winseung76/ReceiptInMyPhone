package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.List;

public class PointListVO {

    private List<PointHistoryVO> list;
    private int datanum;

    public int size(){
        return list.size();
    }
    public PointHistoryVO get(int index){
        return list.get(index);
    }

    public int getDatanum() {
        return datanum;
    }

    public void setDatanum(int datanum) {
        this.datanum = datanum;
    }

    public class PointHistoryVO{

        private int usedpoint;
        private int savedpoint;
        private int totalpoint;
        private String date;

        public int getUsedpoint() {
            return usedpoint;
        }

        public void setUsedpoint(int usedpoint) {
            this.usedpoint = usedpoint;
        }

        public int getSavedpoint() {
            return savedpoint;
        }

        public void setSavedpoint(int savedpoint) {
            this.savedpoint = savedpoint;
        }

        public int getTotalpoint() {
            return totalpoint;
        }

        public void setTotalpoint(int totalpoint) {
            this.totalpoint = totalpoint;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
