package com.tg.pro.urlshortener.pojo.vo;

public class UrlBaseVo {
    private String id;

    private String url;

    private Long view;

    private String grade;

    public UrlBaseVo() {
    }

    public UrlBaseVo(String id,String url, Long view,String grade) {
        this.id = id;
        this.url = url;
        this.view = view;
        this.grade = grade;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getView() {
        return view;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "UrlBaseVo{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", view=" + view +
                ", grade='" + grade + '\'' +
                '}';
    }
}
