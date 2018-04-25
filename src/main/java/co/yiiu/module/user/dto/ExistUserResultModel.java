package co.yiiu.module.user.dto;

import java.util.ArrayList;
import java.util.List;

public class ExistUserResultModel {
    private List<ExistUserModel> existUserModels;
    private boolean result;
    private  String message;

    public  ExistUserResultModel(){
        existUserModels=new ArrayList<ExistUserModel>();
    }


    public List<ExistUserModel> getExistUserModels() {
        return existUserModels;
    }

    public void setExistUserModels(List<ExistUserModel> existUserModels) {
        this.existUserModels = existUserModels;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
