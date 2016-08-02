package com.coolweather.app.util;

import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

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
}
