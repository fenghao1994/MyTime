package com.example.mytime.mvp.ui.view;

/**
 * Created by fenghao on 2016/12/22.
 */

public interface ILoginView {
    //检查电话号码合法性
    boolean checkPhoneNumberValidity(String str);
    //检查验证码的合法性
    boolean checkVerificationCodeValidity(String str);
}
