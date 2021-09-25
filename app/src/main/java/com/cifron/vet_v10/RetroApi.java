package com.cifron.vet_v10;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetroApi {

    @Multipart
    @POST
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);

        @POST("addInfo")
        Call<DataAnimal> uploadText(@Body RequestBody file);

        @POST("addInfo")
        Call<DataAnimalWithImage> uploadAllData(@Body RequestBody file);

    @Multipart
    @POST
    Call<ResponseBody> uploadMultiple(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);

    @Multipart
    @POST
    Call<ResponseBody> uploadThree(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);
}
