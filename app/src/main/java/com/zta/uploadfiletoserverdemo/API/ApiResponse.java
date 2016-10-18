package com.zta.uploadfiletoserverdemo.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zee Abbasi on 10/14/2016.
 */

public class ApiResponse {

    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

}
