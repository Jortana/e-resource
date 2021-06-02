package cn.edu.njnu.pojo;

public class Comment {

    int userID;
    int resourceID;
    String content;
    String date;
    double rate;
    String avatar;

    public Comment() {
    }

    public Comment(int userID, int resourceID, String content, String date, double rate, String avatar) {
        this.userID = userID;
        this.resourceID = resourceID;
        this.content = content;
        this.date = date;
        this.rate = rate;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userID=" + userID +
                ", resourceID=" + resourceID +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", rate=" + rate +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String comment) {
        this.content = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
