package com.newtranx.cloud.edit.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentIndexEntity {

    private String pre;
    private String bian;
    private String zhang;
    private String jie;
    private String contentIndex;
    private String contentName;
    private Integer pageNum;


    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getBian() {
        return bian;
    }

    public void setBian(String bian) {
        this.bian = bian;
    }

    public String getZhang() {
        return zhang;
    }

    public void setZhang(String zhang) {
        this.zhang = zhang;
    }

    public String getJie() {
        return jie;
    }

    public void setJie(String jie) {
        this.jie = jie;
    }

    public String getContentIndex() {
        return contentIndex;
    }

    public void setContentIndex(String contentIndex) {
        this.contentIndex = contentIndex;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "ContentIndexEntity{" +
                "pre='" + pre + '\'' +
                ", bian='" + bian + '\'' +
                ", zhang='" + zhang + '\'' +
                ", jie='" + jie + '\'' +
                ", contentIndex='" + contentIndex + '\'' +
                ", contentName='" + contentName + '\'' +
                ", pageNum=" + pageNum +
                '}';
    }

    public static void main(String[] args) {
        String str = "第一节 革命历史档案.................87";
        String pattern = "[\\u4e00-\\u9fa5]+";
//        String pattern = "^(\\u7b2c).+(\\u8282)(\\s)";
//        String pattern = "^(\\u7b2c).+(\\u8282)";
//        String pattern = "^(\\u7b2c).+(\\u7f16|\\u7ae0|\\u8282)$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if(m.find()){
            System.out.println(m.groupCount());
            System.out.println(m.group());
        }

    }
}
