package com.example.autosellingapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.autosellingapp.items.UserItem;

public class Constant {
    public static final String OPENAPP_ADS_ID = "ca-app-pub-3940256099942544/3419835294";
    public static boolean isInterAd = true;
    public static int adCount = 0;
    public static int adShow = 5;

    public static final String METHOD_UPDATELIKE = "method_updatelike";
    public static final String METHOD_LOGIN = "method_login";
    public static final String ERROR_CON_SERVER = "Error Connecting Server";
    public static final String SUCCESS = "Success";
    public static final String WRONG_LOGIN = "Username or Password wrong!";
    public static final String TEXT_ENGINESIZE= "Engine size";
    public static final String TEXT_GEARS = "Gears";
    public static final String TEXT_CYNLINDER = "Cylinder";
    public static final String TEXT_WEIGHT = "Car weight";
    public static final String TEXT_FUELCONSUMP = "Fuel consumption";
    public static final String TEXT_CO2EMISSTION = "CO2 emission";
    public static final String TEXT_CAR_NAME = "Car name";
    public static final String TEXT_DESCRIPTION = "Description";
    public static final String TEXT_ADDRESS = "Address";
    public static final String METHOD_POST_ADS = "method_post_ads";
    public static final String FAIL = "fail";
    public static final String NO_INTERNET = "No Internet Connection";
    public static final String NO_LOGIN = "You need to login to continue";
    public static final String METHOD_SELLING = "method_selling";
    public static final String METHOD_EDIT_SELLING = "method_edit_selling";
    public static final String METHOD_DELETE_SELLING = "method_delete_selling";
    public static final String TAG_ADS_ISAVAILABLE = "ads_isAvailable";
    public static final String METHOD_MARK_AVAILABLE = "method_mark_available";
    public static final String METHOD_USER_FAVOURITE = "method_user_favourite";
    public static final String MEDTHOD_SIGNUP = "method_signup";
    public static final String METHOD_USER = "method_user";
    public static final String TAG_RECEIVER_UID = "receiver_uid";
    public static final String TAG_CHATLIST = "user_chatlist";
    public static final String METHOD_UPDATE_CHATLIST = "method_update_chatlist";
    public static final String METHOD_PROFILE = "method_profile";
    public static final String PROFILE_MODE = "profile_mode";
    public static final String TAG_FOLLOWLIST = "user_followlist";
    public static final String METHOD_UPDATEFOLLOW = "method_updatefollow";
    public static final String TEXT_FULL_NAME = "Full name";
    public static final String TEXT_PHONE = "Phone";
    public static final String METHOD_UPDATE_USER = "method_update_user";
    public static final String TAG_RECENTADS = "user_recentads";
    public static final String METHOD_HOME = "method_home";
    public static final String METHOD_RECENT = "method_recent_ads";
    public static final String TAG_SEARCH_TEXT = "tag_search_text";
    public static final String TAG_PRICE_MIN = "tag_price_min";
    public static final String TAG_PRICE_MAX = "tag_price_max";
    public static final String TAG_POWER_MIN = "tag_power_min";
    public static final String TAG_POWER_MAX = "tag_power_max";
    public static final String TAG_MILEAGE_MIN = "tag_mileage_min";
    public static final String TAG_MILEAGE_MAX = "tag_mileage_max";
    public static final String TAG_FEEDBACK_MESSAGE = "feedback_message";
    public static final String METHOD_CONTACT = "method_contact";
    public static final String TAG_CAR_VIDEO = "car_video";
    public static final String TEXT_VIDEO = "Video Youtube Link";
    public static final String TAG_CAR_IMAGELIST_LINK = "car_imagelist_link";
    public static final String TAG_VIDEO_TYPE = "video_type";
    public static final String METHOD_REPORTADS = "method_report_ads";
    public static final String TAG_REPORTADS_DESC = "reportads_desc";
    public static boolean isLogged = false;
    public static String UID = "";
    public static final String API_KEY = "dothanhtuan";
    public static final String TAG_ROOT = "ADS_CAR";
    //public static final String SERVER_URL = "http://192.168.1.9/autobuy/";
    public static final String SERVER_URL = "http://radiofree247.com/appcar/";
    public static final String TAG_MANU_ID = "manu_id";
    public static final String TAG_MANU_NAME = "manu_name";
    public static final String TAG_MANU_THUMB = "manu_thumb";
    public static final String METHOD_MANUFACTURER = "manufacturer_list";
    public static final String TAG_MANU = "manufacturer";
    public static final String TAG_MODEL = "model";
    public static final String TAG_CITY = "city";
    public static final String TAG_BODY_TYPE = "bodytype";
    public static final String TAG_FUEL_TYPE = "fueltype";
    public static final String TAG_MODEL_ID = "model_id";
    public static final String TAG_MODEL_NAME = "model_name";
    public static final String TAG_CITY_ID = "city_id";
    public static final String TAG_CITY_NAME = "city_name";
    public static final String TAG_BODY_TYPE_ID = "bodytype_id";
    public static final String TAG_BODY_TYPE_NAME = "bodytype_name";
    public static final String TAG_FUEL_TYPE_ID = "fueltype_id";
    public static final String TAG_FUEL_TYPE_NAME = "fueltype_name";
    public static final String METHOD_SEARCH = "search_method";
    public static final String TEXT_MANU_LIST = "Manufacturer";
    public static final String TEXT_MODEL_LIST = "Model";
    public static final String TEXT_CITY_LIST = "City";
    public static final String TEXT_BODY_TYPE_LIST = "Body type";
    public static final String TEXT_FUEL_TYPE_LIST = "Fuel type";
    public static final String TEXT_CONDITION = "Condition";
    public static final String TAG_TRANS = "transmission";
    public static final String TAG_COLOR = "color";
    public static final String TAG_TRANS_ID = "trans_id";
    public static final String TAG_TRANS_NAME = "trans_name";
    public static final String TAG_COLOR_ID = "color_id";
    public static final String TAG_COLOR_NAME = "color_name";
    public static final String TAG_COLOR_CODE = "color_code";

    public static final String TEXT_TRANS = "Transmission";
    public static final String TEXT_COLOR= "Body color";
    public static final String TEXT_YEAR = "Year";
    public static final String TEXT_SEAT = "Seat number";
    public static final String TEXT_DOOR = "Door number";
    public static final String TEXT_PREUSER = "Previous user number";
    public static final String TEXT_PRICE = "Price";
    public static final String TEXT_POWER = "Power";
    public static final String TEXT_MILEAGE = "Mileage";
    public static final String TAG_EQUIP = "equip";
    public static final String TAG_EQUIP_ID = "equip_id";
    public static final String TAG_EQUIP_NAME = "equip_name";
    public static final String TEXT_EQUIPMENT = "Equipment";
    public static final String TAG_ADS = "ads";
    public static final String TAG_CAR = "car";
    public static final String TAG_ADS_ID = "ads_id";
    public static final String TAG_CAR_ID = "car_id";
    public static final String TAG_UID = "user_id";
    public static final String TAG_ADS_PRICE = "ads_price";
    public static final String TAG_ADS_MILEAGE = "ads_mileage";
    public static final String TAG_ADS_LOCATION = "ads_location";
    public static final String TAG_ADS_DESCRIPTION = "ads_description";
    public static final String TAG_ADS_POST_TIME = "ads_posttime";
    public static final String TAG_ADS_LIKE = "ads_likes";
    public static final String TAG_CAR_NAME = "car_name" ;
    public static final String TAG_CAR_IMAGELIST = "car_imagelist";
    public static final String TAG_CAR_YEAR ="car_year" ;
    public static final String TAG_CAR_CONDITION = "car_condition";
    public static final String TAG_CAR_POWER = "car_power";
    public static final String TAG_CAR_DOORS = "car_doors";
    public static final String TAG_CAR_SEATS = "car_seats";
    public static final String TAG_CAR_EQUIP = "car_equipments";
    public static final String TAG_CAR_PREOWNER = "car_previousowner";
    public static final String TAG_CAR_GEARS = "car_gears";
    public static final String TAG_CAR_ENGINESIZE = "car_enginesize";
    public static final String TAG_CAR_CYLINDER = "car_cylinder";
    public static final String TAG_CAR_KERBWEIGHT = "car_kerbweight";
    public static final String TAG_CAR_FUELCONSUMP = "car_fuelconsump";
    public static final String TAG_CAR_CO2EMISSION = "car_co2emission";
    public static final String METHOD_CATEGORY = "category_method";
    public static final String TAG_USER = "user";
    public static final String TAG_PASSWORD = "user_password";
    public static final String TAG_ADDRESS = "user_address";
    public static final String TAG_PHONE = "user_phone";
    public static final String TAG_FULLNAME = "user_fullname";
    public static final String TAG_EMAIL = "user_email";
    public static final String TAG_FAVLIST = "user_favlist";
    public static final String TAG_USER_IMAGE = "user_image";
    public static final int MY_PROFILE = 96;
    public static final int USER_PROFILE = 445;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
