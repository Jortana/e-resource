package cn.edu.njnu.pojo;

import java.util.Arrays;

public class Resource {
    int id;
    String resourceName;
    String remark;

    String url;
    String viewUrl;

    String aid;
    String bvid;
    String cid;

    String related_10;
    String keywords;
    String entity;
    String[] entityList;
    String updateTime;
    int download;
    int collection;
    int browse;
    int resourceType;
    String period;
    String grade;
    int subject;

    int table;
    Integer tableResourceID;

    public Resource() {
    }

    public Resource(int id, String resourceName, String remark, String url, String viewUrl, String aid, String bvid, String cid, String related_10, String keywords, String entity, String[] entityList, String updateTime, int download, int collection, int browse, int resourceType, String period, String grade, int subject, int table, Integer tableResourceID) {
        this.id = id;
        this.resourceName = resourceName;
        this.remark = remark;
        this.url = url;
        this.viewUrl = viewUrl;
        this.aid = aid;
        this.bvid = bvid;
        this.cid = cid;
        this.related_10 = related_10;
        this.keywords = keywords;
        this.entity = entity;
        this.entityList = entityList;
        this.updateTime = updateTime;
        this.download = download;
        this.collection = collection;
        this.browse = browse;
        this.resourceType = resourceType;
        this.period = period;
        this.grade = grade;
        this.subject = subject;
        this.table = table;
        this.tableResourceID = tableResourceID;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", remark='" + remark + '\'' +
                ", url='" + url + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", aid='" + aid + '\'' +
                ", bvid='" + bvid + '\'' +
                ", cid='" + cid + '\'' +
                ", related_10='" + related_10 + '\'' +
                ", keywords='" + keywords + '\'' +
                ", entity='" + entity + '\'' +
                ", entityList=" + Arrays.toString(entityList) +
                ", updateTime='" + updateTime + '\'' +
                ", download=" + download +
                ", collection=" + collection +
                ", browse=" + browse +
                ", resourceType=" + resourceType +
                ", period='" + period + '\'' +
                ", grade='" + grade + '\'' +
                ", subject=" + subject +
                ", table=" + table +
                ", tableResourceID=" + tableResourceID +
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

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getBvid() {
        return bvid;
    }

    public void setBvid(String bvid) {
        this.bvid = bvid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
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

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public Integer getTableResourceID() {
        return tableResourceID;
    }

    public void setTableResourceID(Integer tableResourceID) {
        this.tableResourceID = tableResourceID;
    }
}
