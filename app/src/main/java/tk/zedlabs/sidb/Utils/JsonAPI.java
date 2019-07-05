package tk.zedlabs.sidb.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import tk.zedlabs.sidb.POJO.DataResponse;
import tk.zedlabs.sidb.POJO.DataResponseEducation;
import tk.zedlabs.sidb.POJO.DataResponsePersonal;
import tk.zedlabs.sidb.POJO.DataResponseProfessional;
import tk.zedlabs.sidb.POJO.Education;
import tk.zedlabs.sidb.POJO.Personal;
import tk.zedlabs.sidb.POJO.Professional;
import tk.zedlabs.sidb.POJO.Qse;
import tk.zedlabs.sidb.POJO.Test;
import tk.zedlabs.sidb.POJO.User;

public interface JsonAPI {

    @GET("/test")
    Call<Test> getTestResult();

    @POST("/user/signup")
    Call<DataResponse> createUser(@Body User user);

    @POST("/user/login")
    Call<DataResponse> loginUser(@Body User user);

    @POST("/user/personaldetail/{id}")
    Call<DataResponsePersonal> setPersonalDetails(@Path("id") int id, @Body Personal personal);

    @GET("/user/personaldetail/{id}")
    Call<DataResponsePersonal> getPersonalDetails(@Path("id") int id);

    @GET("/user/personaldetail/profilepic/{id}")
    Call<ResponseBody> getProfilePicture(@Path("id") int id);

    @GET("/user/educationdetail/certificate/{id}")
    Call<ResponseBody> getCertificate(@Path("id") int id);

    @POST("/user/educationdetail/{id}")
    Call<DataResponseEducation> setEducationDetails(@Path("id") int id, @Body Education education);

    @GET("/user/educationdetail/{id}")
    Call<DataResponseEducation> getEducationDetails(@Path("id") int id);

    @GET("/user/professionaldetail/{id}")
    Call<DataResponseProfessional> getProfessionalDetails(@Path("id") int id);

    @POST("/user/professionaldetail/{id}")
    Call<DataResponseProfessional> setProfessionalData(@Path("id") int id, @Body Professional professional);

    @DELETE("/user/educationdetail/{id}")
    Call<ResponseBody> deleteEducationDetails(@Path("id") int id);

    @DELETE("/user/professionaldetail/{id}")
    Call<ResponseBody> deleteProfessionalDetails(@Path("id") int id);

    @Multipart
    @POST("/user/personaldetail/profilepic")
    Call<Qse> setProfilePicture(
            @Part MultipartBody.Part photo,
            @Part("uid") RequestBody uid
    );

    @Multipart
    @POST("/user/educationdetail/certificate")
    Call<Qse> setCertificate(
            @Part MultipartBody.Part photo,
            @Part("uid") RequestBody uid
    );

}
