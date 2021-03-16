package cn.edu.njnu.pojo;

public class Resource {
    int id;
    String resourceName;
    String remark;
    String url;
    String related_10;
    String keywords;
    String entity;
    int download;
    int collection;
    int type;
    int period;
    int grade;
    int subject;

    public Resource() {
    }

    public Resource(int id, String resourceName, String remark, String url, String related_10, String keywords, String entity, int download, int collection, int type, int period, int grade, int subject) {
        this.id = id;
        this.resourceName = resourceName;
        this.remark = remark;
        this.url = url;
        this.related_10 = related_10;
        this.keywords = keywords;
        this.entity = entity;
        this.download = download;
        this.collection = collection;
        this.type = type;
        this.period = period;
        this.grade = grade;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", remark='" + remark + '\'' +
                ", url='" + url + '\'' +
                ", related_10='" + related_10 + '\'' +
                ", keywords='" + keywords + '\'' +
                ", entity='" + entity + '\'' +
                ", download=" + download +
                ", collection=" + collection +
                ", type=" + type +
                ", period=" + period +
                ", grade=" + grade +
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }
}
