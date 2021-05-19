package cn.edu.njnu.pojo;

import java.util.Arrays;

public class Resource {
    int id;
    String resourceName;
    String remark;
    String url;
    String viewUrl;
    String related_10;
    String keywords;
    String entity;
    String[] entityList;
    String updateTime;
    int download;
    int collection;
    int type;
    String period;
    String grade;
    int subject;

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", remark='" + remark + '\'' +
                ", url='" + url + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", related_10='" + related_10 + '\'' +
                ", keywords='" + keywords + '\'' +
                ", entity='" + entity + '\'' +
                ", entityList=" + Arrays.toString(entityList) +
                ", updateTime='" + updateTime + '\'' +
                ", download=" + download +
                ", collection=" + collection +
                ", type=" + type +
                ", period='" + period + '\'' +
                ", grade='" + grade + '\'' +
                ", subject=" + subject +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getRelated_10() {
        return related_10;
    }

    public void setRelated_10(String related_10) {
        this.related_10 = related_10;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String[] getEntityList() {
        return entityList;
    }

    public void setEntityList(String[] entityList) {
        this.entityList = entityList;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public Resource() {
    }

    public Resource(int id, String resourceName, String remark, String url, String viewUrl, String related_10, String keywords, String entity, String[] entityList, String updateTime, int download, int collection, int type, String period, String grade, int subject) {
        this.id = id;
        this.resourceName = resourceName;
        this.remark = remark;
        this.url = url;
        this.viewUrl = viewUrl;
        this.related_10 = related_10;
        this.keywords = keywords;
        this.entity = entity;
        this.entityList = entityList;
        this.updateTime = updateTime;
        this.download = download;
        this.collection = collection;
        this.type = type;
        this.period = period;
        this.grade = grade;
        this.subject = subject;
    }
}
