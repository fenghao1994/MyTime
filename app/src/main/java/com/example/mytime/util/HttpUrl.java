package com.example.mytime.util;

/**
 * Created by fenghao on 2017/5/7.
 */

public class HttpUrl {
    public static final String ROOT = "http://192.168.191.1:8080";

    //上传save Note
    public static final String POST_SAVE_NOTE = ROOT + "/note/saveNote";
    //上传updata Note
    public static final String POST_UPDATA_NOTE = ROOT + "/note/updateNote";
    //获取note
    public static final String POST_GET_NOTE = ROOT + "/note/getNote";
    //删除note
    public static final String POST_DELETE_NOTE = ROOT + "/note/deleteNote";

    //上传 save planitem
    public static final String POST_SAVE_PLAN_ITEM = ROOT + "/planItem/savePlanItem";

    //上传 updata planitem
    public static final String POST_UPDATA_PLAN_ITEM = ROOT + "/planItem/updatePlanItem";

    //获取plan item
    public static final String POST_GET_PLAN_ITEM = ROOT + "/planItem/getPlanItem";

    //删除planitem
    public static final String POST_DELETE_PLAN_ITEM = ROOT + "/planItem/deletePlanItem";

    //完成planitem
    public static final String POST_COMPLETE_PLAN_ITEM = ROOT + "/planItem/completePlanItem";

    //上传save plan
    public static final String POST_SAVE_PLAN = ROOT + "/plan/savePlan";

    //上传updata plan
    public static final String POST_UPDATA_PLAN = ROOT + "/plan/updatePlan";

    //获取plan
    public static final String POST_GET_PLAN = ROOT + "/plan/getPlan";

    //重置密码
    public static final String RESET_PASSWORD = ROOT + "/user/reset/password";

    //忘记密码
    public static final String POST_FORGET_PASSWORD = ROOT + "/user/forget/password";

    //注册
    public static final String POST_REGISTER = ROOT + "/user/register";

    //登陆
    public static final String POST_LOGIN = ROOT + "/user/login";

    //获取头像
    public static final String POST_GET_HEADIMG = ROOT + "/user/get/headimg";
    //上传头像
    public static final String POST_HEADER_IMG = ROOT + "/user/upload/headimg";

    //上传反馈意见
    public static final String POST_FEED_BACK = ROOT + "/user/feed_back";

    //上传日记
    public static final String POST_RIJI = ROOT + "/riji/save_riji";

    //获取所有的日记
    public static final String POST_ALL_RIJI = ROOT + "/riji/getAllRiji";

    //分享
    public static final String GET_SHARE_PLANITEM = ROOT + "/share/planitem";

    //获取朋友公开的计划
    public static final String POST_FRIEND_OPEN = ROOT + "/share/friend/open";

    //更新用户信息
    public static final String POST_USER_MESSAGE = ROOT + "/user/user_message";

    //获取朋友列表
    public static final String POST_FRIEND_LIST = ROOT + "/share/friends";

    //改变朋友关系
    public static final String POST_CHANGE_FRIEND_RELATIONSHIP = ROOT + "/share/change/friend/relationship";

    //获取某人的公开计划
    public static final String POST_GET_SOMEONE_OPEN_PLANITEM = ROOT + "/share/get/someone/share";

    //保存label
    public static final String POST_SAVE_USER_LABEL = ROOT + "/user/user_label";

    //删除label
    public static final String POST_DELETE_USER_LABEL = ROOT + "/user/delete/label";

    //发表评论
    public static final String POST_SEND_PINGLUN = ROOT + "/planItem/pinglun";

    //获取一个计划的所有评论
    public static final String POST_GET_ALL_PING_LUN = ROOT + "/planItem/get/pinglun";

    //更新location字段
    public static final String POST_UPDATE_LOCATION = ROOT + "/planItem/update/location";
}
