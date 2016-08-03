package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class Utility {
    /**
     * @param coolWeatherDB
     * @param response
     * @return   解析和处理服务器返回来的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
        if (!TextUtils.isEmpty(response)){
//            Log.e("-------------", "handleProvincesResponse: "+response );
            String [] allProvinces=response.split(",");
            if (allProvinces!=null&&allProvinces.length>0){
//                Log.e("--------", "handleProvincesResponse:2132131 ");
                for (String p:allProvinces){
//                    Log.e("-----------", "handleProv " );
                    String [] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
        public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
            if (!TextUtils.isEmpty(response)){
                String []allCities=response.split(",");
                if (allCities!=null&&allCities.length>0){
                    for (String c:allCities){
                        String [] array=c.split("\\|");
                        City city=new City();
                        city.setCityCode(array[0]);
                        city.setCityName(array[1]);
                        city.setProvinceId(provinceId);
                        coolWeatherDB.saveCity(city);
                    }
                    return true;
                }
            }
            return false;
        }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String [] allCounties=response.split(",");
            if (allCounties!=null&&allCounties.length>0){
                for (String c:allCounties){
                    String [] array=c.split("\\|");
                    County county=new County();
                    county.setCountyName(array[1]);
                    county.setCountyCode(array[0]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     *解析服务器返回的json数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context,String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weatherCode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的天气存储到sp文件中
     */
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }


}
