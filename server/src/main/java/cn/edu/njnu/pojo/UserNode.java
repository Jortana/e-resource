package cn.edu.njnu.pojo;

import java.util.List;

public class UserNode {
    private int id;
    private List<UserNode> userNodes;

    public UserNode() {
    }

    public UserNode(int id, List<UserNode> userNodes) {
        this.id = id;
        this.userNodes = userNodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<UserNode> getUserNodes() {
        return userNodes;
    }

    public void setUserNodes(List<UserNode> userNodes) {
        this.userNodes = userNodes;
    }

    @Override
    public String toString() {
        return "UserNode{" +
                "id=" + id +
                ", userNodes=" + userNodes +
                '}';
    }
}
