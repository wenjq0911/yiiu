package co.yiiu.module.user.dto;

public class ExistUserModel {
    private int id;
    // 用户名
    private String username;
    //原来的真实名称
    private String existRealName;
    //现在的真实名称
    private String newRealName;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String errorMsg;

    public int getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(int errorStatus) {
        this.errorStatus = errorStatus;
    }

    private int errorStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExistRealName() {
        return existRealName;
    }

    public void setExistRealName(String existRealName) {
        this.existRealName = existRealName;
    }

    public String getNewRealName() {
        return newRealName;
    }

    public void setNewRealName(String newRealName) {
        this.newRealName = newRealName;
    }
}
