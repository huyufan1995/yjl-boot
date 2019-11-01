package io.renren.api.vo.box;

import lombok.Data;

public class Box1 {

    private String bg;
    private String mp_bg;
    public void setBg(String bg) {
        this.bg = bg;
    }
    public String getBg() {
        return bg;
    }

    public void setMp_bg(String mp_bg) {
        this.mp_bg = mp_bg;
    }
    public String getMp_bg() {
        return mp_bg;
    }

    public Box1(String bg, String mp_bg) {
        this.bg = bg;
        this.mp_bg = mp_bg;
    }
}