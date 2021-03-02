package com.bukcoja.seung.receipt_inmyhand.VO;

import java.util.List;

public class CategoryListVO {

    private List<Category> list;

    public Category get(int index){return list.get(index);}

    public int size(){return list.size();}

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }

    public class Category{
        private int categoryid;
        private String category;

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
