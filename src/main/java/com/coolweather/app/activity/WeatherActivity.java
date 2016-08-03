package com.coolweather.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class WeatherActivity extends Activity {
    private TextView cityNameText;
    private TextView publishText;
    private TextView currentDateText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private LinearLayout weatherInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        initView();
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);

        }else{
            showWeather();
        }
    }

    private void queryWeatherCode(String countyCode) {
        String adress="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(adress,"countyCode");
    }
    private void queryWeatherInfo(String weatherCode){
        String adress="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(adress,"weatherCode");
    }

    private void queryFromServer(String adress, final String type) {
        HttpUtil.sendHttpRequest(adress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)){
                    if (!TextUtils.isEmpty(response)){
                        String []array=response.split("\\|");
                        if (array!=null&&array.length==2){
                            String weatherCode=array[1];
//                            Toast.makeText(getApplicationContext(),"oooo",Toast.LENGTH_SHORT).show();
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onErroe(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });

    }

    private void showWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date",""));
        cityNameText.setVisibility(View.VISIBLE);
    }

    private void initView() {
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        currentDateText = (TextView) findViewById(R.id.current_date);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
    }



}
