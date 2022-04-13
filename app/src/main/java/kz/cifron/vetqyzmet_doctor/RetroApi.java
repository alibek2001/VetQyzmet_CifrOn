package kz.cifron.vetqyzmet_doctor;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetroApi {

    @POST("addInfo")
    Call<DataAnimalWithImage> uploadAllData(@Body RequestBody file);


    @Multipart
    @POST
    Call<ResponseBody> uploadMultiple(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);

}
