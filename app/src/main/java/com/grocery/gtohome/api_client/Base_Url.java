package com.grocery.gtohome.api_client;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Base_Url {

   // String BaseUrl="http://gtohome.in/index.php?route=account/";
    String BaseUrl="https://gtohome.in/";

    String Register="index.php?route=restapi/auth/register";
    String loginapi="index.php?route=restapi/auth/login";
    String customer_logout="index.php?route=restapi/auth/customer_logout";
    String forgottenapi="account/forgottenapi";
    String categoriesapi="restapi/category";
    String category_product="restapi/product/category_product";


}
