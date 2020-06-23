package com.example.calculatedtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;

/**
 * Created by yang
 * date: 2020/6/17
 * Describe: 算式处理
 */
public class MyViewModel extends AndroidViewModel {

    private SavedStateHandle handle;
    private static String KEY_HIGH_SCORE = "key_high_score";
    private static String KEY_LEFT_NUMBER = "key_left_number";
    private static String KEY_RIGHT_NUMBER ="key_right_number";
    private static String KEY_OPERATOR = "key_operator";
    private static String KEY_ANSWER = "key_answer";
    private static String KEY_SHP_DATA_NAME = "key_shp_data_name";
    private static String KEY_CURRENT_SCORE = "key_current_score";
    boolean win_flag = false;

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        //判断刚开始进入时候是否有值，没有就初始化为0
        if (!handle.contains(KEY_HIGH_SCORE)){
            SharedPreferences shp = getApplication().getSharedPreferences(KEY_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE,shp.getInt(KEY_HIGH_SCORE,0));
            handle.set(KEY_LEFT_NUMBER,0);
            handle.set(KEY_RIGHT_NUMBER,0);
            handle.set(KEY_OPERATOR,"+");
            handle.set(KEY_ANSWER,0);
            handle.set(KEY_CURRENT_SCORE,0);
        }
        this.handle = handle;
    }
    //获取对应数的值
    public MutableLiveData<Integer> getHighScore(){
        return handle.getLiveData(KEY_HIGH_SCORE);
    }
    public MutableLiveData<Integer> getLeftNumber(){
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }
    public MutableLiveData<Integer> getRightNumber(){
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }
    public MutableLiveData<String> getOperator(){
        return handle.getLiveData(KEY_OPERATOR);
    }
    public MutableLiveData<Integer> getAnswer(){
        return handle.getLiveData(KEY_ANSWER);
    }
    public MutableLiveData<Integer> getCurrentScore(){
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }
    //随机生成一个算式
    public void generator(){
        //定义一个随机数的范围
        int LEVE = 100;
        int x,y;
        //定义一个随机数
        Random random = new Random();
        x = random.nextInt(LEVE) + 1;
        y = random.nextInt(LEVE) + 1;
        //如果是偶数就是加，奇数就是减
        if (x%2 == 0){
            getOperator().setValue("+");
            if (x > y){
                getAnswer().setValue(x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x -y);
            } else {
                getAnswer().setValue(y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y - x);
            }
        } else {
            getOperator().setValue("-");
            if (x > y){
                getAnswer().setValue(x -y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
            }else {
                getAnswer().setValue(y - x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
            }
        }
    }

    //保存最高分数
    public void save(){
        SharedPreferences shp = getApplication().getSharedPreferences(KEY_SHP_DATA_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE,getHighScore().getValue());
        editor.apply();
    }
    //答对处理
    public void answerCorrent(){
        //答对当前分数+1
        getCurrentScore().setValue(getCurrentScore().getValue()+1);
        //如果当前分数比最高分数高，则最高分数设置为当前分数
        if (getCurrentScore().getValue() > getHighScore().getValue()){
            getHighScore().setValue(getCurrentScore().getValue());
            //给个标志，标识答对
            win_flag = true;
        }
        //重新生成一道算式
        generator();
    }
}
