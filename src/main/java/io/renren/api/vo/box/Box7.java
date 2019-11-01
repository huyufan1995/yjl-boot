package io.renren.api.vo.box;

public class Box7 {

    private String bg;
    private String box;
    private String btn;
    public void setBg(String bg) {
        this.bg = bg;
    }
    public String getBg() {
        return bg;
    }

    public void setBox(String box) {
        this.box = box;
    }
    public String getBox() {
        return box;
    }

    public Box7(String bg, String box) {
        this.bg = bg;
        this.box = box;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public Box7(String bg, String box, String btn) {
        this.bg = bg;
        this.box = box;
        this.btn = btn;
    }
}
