package com.grocery.gtohome.api_client;

import com.grocery.gtohome.model.FilterByModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
import com.grocery.gtohome.model.product_details.Product_Details_Model;
import com.grocery.gtohome.model.register_model.RegistrationModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Api_Call {

    @GET("index.php?")
    Observable<RegistrationModel> EmailRegistrationUser(
            @Query("route") String path,
            @Query("firstname") String first_name,
            @Query("lastname") String last_name,
            @Query("password") String et_pw,
            @Query("confirm") String etConf_pw,
            @Query("telephone") String et_mobile,
            @Query("email") String et_email);


    @GET("index.php?")
    Observable<RegistrationModel> LoginUser(
            @Query("route") String loginapi,
            @Query("email") String et_email,
            @Query("password") String et_pw);


    @GET("index.php?")
    Observable<SimpleResultModel> ForgotUser(
            @Query("route") String forgottenapi,
            @Query("email") String et_email);

    @GET("index.php?route=api/category")
    Observable<CategoryModel> CategoryApi(
           );


    @POST("index.php?route=api/product/category_product")
    @FormUrlEncoded
    Observable<CategoryProductModel> CategoryProductApi(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=api/product/product_detail")
    Observable<Product_Details_Model> ProductDetails(
            @Field ("product_id")String userid);


    @GET("index.php?route=api/filter/sortby")
    Observable<FilterByModel> FilterByApi();

    @FormUrlEncoded
    @POST("index.php?route=api/cart/add")
    Observable<SimpleResultModel>  AddCart(@FieldMap HashMap<String, String> map);
}
