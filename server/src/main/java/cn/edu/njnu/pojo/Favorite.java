package cn.edu.njnu.pojo;

public class Favorite {
    int userID;

    int folderID;
    String folderName;
    int folderSort;

    int resourceID;
    String resourceName;
    String fileType;

    public Favorite() {
    }

    public Favorite(int userID, int folderID, String folderName, int folderSort, int resourceID, String resourceName, String fileType) {
        this.userID = userID;
        this.folderID = folderID;
        this.folderName = folderName;
        this.folderSort = folderSort;
        this.resourceID = resourceID;
        this.resourceName = resourceName;
        this.fileType = fileType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getFolderSort() {
        return folderSort;
    }

    public void setFolderSort(int folderSort) {
        this.folderSort = folderSort;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "userID=" + userID +
                ", folderID=" + folderID +
                ", folderName='" + folderName + '\'' +
                ", folderSort=" + folderSort +
                ", resourceID=" + resourceID +
                ", resourceName='" + resourceName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
