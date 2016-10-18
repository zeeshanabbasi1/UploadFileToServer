package com.zta.uploadfiletoserverdemo.API;

import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by Zee Abbasi on 10/14/2016.
 */

public interface ApiService {

    @Multipart
    @POST("uploadFile.php")
    Call<ApiResponse> uploadFileService(@PartMap Map<String, RequestBody> map);

}
