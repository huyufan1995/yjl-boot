package io.renren.api.vo.box;

import lombok.Data;


public class Box3 {

    private String top;
    private String bg;
    public void setTop(String top) {
        this.top = top;
    }
    public String getTop() {
        return top;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }
    public String getBg() {
        return bg;
    }

    public Box3(String top, String bg) {
        this.top = top;
        this.bg = bg;
    }
}
