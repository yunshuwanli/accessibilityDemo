package com.mmo.accessibilitydemo.script;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.util.LogUtil;
import com.mmo.accessibilitydemo.UiApplication;

import java.util.List;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/23
 *     desc  : 前往 设置->移动网络界面 脚本
 * </pre>
 */
public class ToNetPageScript {

    public static void doWrok() {
        /*首先让手机回退到桌面,注意:由于每个手机的桌面包名不一致,
        请添加你的测试机桌面包名至com.auto.assist.accessibility.util.Config中,
        否者无法成功回退*/
        UiApi.backToDesk();

        //启动设置应用
        startSetting();

        //
        doAction();

    }

    private static void doAction() {
        if (isSettingPage()) {
            //已进入设置界面
            LogUtil.D("已在设置界面");

            //前往移动网络界面
            if (toNetPage()) {
                LogUtil.D("前往移动网络页面成功");

            } else {
                LogUtil.E("前往移动网络页面异常,暂停操作");

            }


        } else {
            LogUtil.E("当前不在设置界面,暂停操作");
        }

    }

    //是否在设置界面
    private static boolean isSettingPage() {

        String pageStr = "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['设置'],'id':[],'desc':[]},"
                + "'option':{'text':['无线和网络'],'id':[],'desc':[]}"
                + "}";

        return UiApi.isMyNeedPage(pageStr);

    }

    //前往移动网络页面
    private static boolean toNetPage() {
        String temp1 = "{"
                + "'maxWClickMSec':1000,"
                + "'click':{'text':'更多'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['更多','设置'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";

        String temp2 = "{"
                + "'maxWClickMSec':1000,"
                + "'click':{'text':'移动网络'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['移动网络','更多'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";


        return UiApi.jumpToNeedPage(new String[]{temp1, temp2});

    }

    private static void startSetting() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UiApplication.context.startActivity(intent);
    }

    private static void startDOUYIN() {
        doStartApplicationWithPackageName(UiApplication.context,"com.ss.android.ugc.aweme");
    }

    public static void doStartApplicationWithPackageName(Context context, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (packageinfo == null) {
            LogUtil.E("未集成该模块");
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

}
