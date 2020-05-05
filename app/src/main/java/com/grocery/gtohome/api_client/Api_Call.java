package com.grocery.gtohome.api_client;

import com.grocery.gtohome.fragment.country_model.CountryModel;
import com.grocery.gtohome.fragment.state_model.StateModel;
import com.grocery.gtohome.model.FilterByModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.address_model.AddressModel;
import com.grocery.gtohome.model.address_model.SaveAddressModel;
import com.grocery.gtohome.model.cart_model.CartModel;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
import com.grocery.gtohome.model.create_order.CreateOrderModel;
import com.grocery.gtohome.model.product_details.Product_Details_Model;
import com.grocery.gtohome.model.register_model.RegistrationModel;
import com.grocery.gtohome.model.shipping_method.ShippingMethod;

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

    @GET("index.php?route=restapi/category")
    Observable<CategoryModel> CategoryApi(
           );


    @POST("index.php?route=restapi/product/category_product")
    @FormUrlEncoded
    Observable<CategoryProductModel> CategoryProductApi(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/product/product_detail")
    Observable<Product_Details_Model> ProductDetails(
            @Field ("product_id")String userid);


    @GET("index.php?route=restapi/filter/sortby")
    Observable<FilterByModel> FilterByApi();

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/add")
    Observable<SimpleResultModel>  AddCart(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/view")
    Observable<CartModel>  CartListApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/remove")
    Observable<SimpleResultModel>  RemoveCartItemApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/edit")
    Observable<SimpleResultModel> UpdateCartItemApi(@FieldMap  HashMap<String, String> map);

    @GET("index.php?route=restapi/product/featured_products")
    Observable<CategoryProductModel> FeatureProductApi();


    @GET("index.php?route=restapi/customer")
    Observable<AddressModel> GetExistingAddress( @Query("customer_id") String customer_id);


    @GET("index.php?route=restapi/filter/countries")
    Observable<CountryModel> GetCountry();

    @GET("index.php?route=restapi/filter/states")
    Observable<StateModel>  GetState(@Query("country_id")String country_id);


    @GET("index.php?route=restapi/customer/save_payment_address")
    Observable<SaveAddressModel> SaveDeliveryAddress(
            @Query("customer_id") String customer_id,
            @Query("firstname")  String first_name,
            @Query("lastname") String last_name,
            @Query("company") String company_name,
            @Query("address_1") String address1,
            @Query("address_2") String address2,
            @Query("city") String city,
            @Query("postcode") String postcode,
            @Query("country_id") String country_id,
            @Query("zone_id") String zone_id);


    @FormUrlEncoded
    @POST("index.php?route=restapi/shipping/methods")
    Observable<ShippingMethod>  GetShippingMethod(
            @Field ("total") String totalPrice,
            @Field ("country_id")  String countryId,
            @Field ("zone_id") String zoneId);


    @FormUrlEncoded
    @POST("index.php?route=restapi/orders/add")
    Observable<CreateOrderModel>  CreateOrder(@FieldMap HashMap<String, String> map);
}
