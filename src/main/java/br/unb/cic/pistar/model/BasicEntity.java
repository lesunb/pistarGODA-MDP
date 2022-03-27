package br.unb.cic.pistar.model;

public abstract class BasicEntity extends BaseEntity {

    private String text;
    private Integer x;
    private Integer y;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}