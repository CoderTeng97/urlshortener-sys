package com.tg.pro.urlshortener.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Teng
 * @since 2019-04-10
 */

@TableName("tb_url_manager")
public class UrlManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id 短地址
     */
    @TableId(value = "id")
    private String id;


    /**
     * 长地址url
     */
    private String url;

    /**
     * 访问量
     */
    private Long view;

    /**
     * 备注
     */
    private String remark;

    /**
     * 访问频率等级
     */
    private String grade;

    /**
     * s是否删除
     */
    private Integer isDel;

    /**
     * 注册时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModified;


    public UrlManager() {
    }

    public UrlManager(String id, String url, String remark, String grade) {
        this.id = id;
        this.url = url;
        this.remark = remark;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "UrlManager{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", view=" + view +
                ", remark='" + remark + '\'' +
                ", grade='" + grade + '\'' +
                ", isDel=" + isDel +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
