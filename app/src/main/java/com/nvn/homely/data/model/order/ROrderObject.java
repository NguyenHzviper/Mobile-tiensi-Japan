
package com.nvn.homely.data.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ROrderObject {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("data")
    @Expose
    private OrderObject data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public OrderObject getData() {
        return data;
    }

    public void setData(OrderObject data) {
        this.data = data;
    }

}