package com.grocery.gtohome.api_client;

import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.register_model.RegistrationModel;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Api_Call {

    @GET("index.php?")
    Observable<RegistrationModel>  EmailRegistrationUser(
            @Query("route") String path,
            @Query("firstname") String first_name,
            @Query("lastname")String last_name,
            @Query("password") String et_pw,
            @Query("confirm") String etConf_pw,
            @Query("telephone") String et_mobile,
            @Query("email") String et_email);



    @GET("index.php?")
    Observable<RegistrationModel>LoginUser(
            @Query("route") String loginapi,
            @Query("email")  String et_email,
            @Query("password") String et_pw);


    @GET("index.php?")
    Observable<SimpleResultModel> ForgotUser(
            @Query("route")  String forgottenapi,
            @Query("email") String et_email);

    @GET("index.php?")
    Observable<CategoryModel> CategoryApi(
            @Query("route") String categoriesapi);

//    @Headers("API-KEY: 123456")
//    @GET(products)
//    Observable<LanyardProductsModel> GetLanyardProduct();
//
//
//    @FormUrlEncoded
//    @Headers("API-KEY: 123456")
//    @POST(add_to_cart)
//    Observable<AddCartModel> AddCartProduct(
//            @Field("userid")String userid,
//            @Field("product_id") String productId);
}
