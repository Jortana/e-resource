package cn.edu.njnu.pojo;

public class Comment {

    int userID;
    int resourceID;
    String content;
    String date;
    double rate;
    String avatar;
    String userName;

    public Comment() {
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
                ", userName='" + userName + '\'' +
                '}';
    }

    public Comment(int userID, int resourceID, String content, String date, double rate, String avatar, String userName) {
        this.userID = userID;
        this.resourceID = resourceID;
        this.content = content;
        this.date = date;
        this.rate = rate;
        this.avatar = avatar;
        this.userName = userName;
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

    public void setContent(String content) {
        this.content = content;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
