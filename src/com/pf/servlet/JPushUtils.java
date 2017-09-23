package com.pf.servlet;

import Tool.ReadConfig;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xwy_brh on 2017/8/15.
 */
public class JPushUtils {

    private String appKey;
    private String masterSecret;
    private JPushClient jpush = null;
    private long timeToLive =  60 * 60 * 24;

    private static JPushUtils sharedInstance;
    public  static JPushUtils getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new JPushUtils();
        }
        return sharedInstance;
    }

    public JPushUtils() {
        this.appKey = new ReadConfig().getprop().getProperty("JPush_appKey");
        this.masterSecret = new ReadConfig().getprop().getProperty("JPush_masterSecret");
    }


    public boolean sendAll(String title, String content) {
        System.out.println(this.appKey+"\n"+this.masterSecret);
        jpush = new JPushClient(this.masterSecret, this.appKey, timeToLive);

        String sendNo = ""+this.getRandomSendNo();
        String msgTitle = title;
        String msgContent = content;

        Map<String, Object> extra = new HashMap<String, Object>();
        IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
        iosExtra.setContent(msgContent);
        iosExtra.setTitle(msgTitle);
        extra.put("ios", iosExtra);
        extra.put("title","������Ϣ");

        HashMap<String, String> map=new HashMap<String, String>();
        map.put("title", title);
        map.put("body", msgContent);

        MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 1, extra);

        if (msgResult != null) {
            //System.out.println("��������������: " + msgResult.toString());
            if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
                System.out.println("���ͳɹ��� sendNo=" + msgResult.getSendno());
            } else {
                System.out.println("����ʧ�ܣ� �������=" + msgResult.getErrcode() + ", ������Ϣ=" + msgResult.getErrmsg());
            }
        } else {
            System.out.println("�޷���ȡ����");
        }
        return false;
    }

    public boolean sendByAlias(String title, String content, String alias) {
        String sendNo = ""+this.getRandomSendNo();
        String msgTitle = title;
        String msgContent = content;
        jpush = new JPushClient(this.masterSecret, this.appKey, timeToLive);

        Map<String, Object> extra = new HashMap<String, Object>();
        IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
        iosExtra.setContent(msgContent);
        iosExtra.setTitle(msgTitle);
        extra.put("ios", iosExtra);
        extra.put("title","������Ϣ");

        MessageResult msgResult=jpush.sendNotificationWithAlias(sendNo, alias, msgTitle, msgContent, 3,extra);
        if (msgResult != null) {
            System.out.println("��������������: " + msgResult.toString());
            if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
                System.out.println("���ͳɹ���sendNo=" + msgResult.getSendno());
            } else {
                System.out.println("����ʧ�ܣ� �������=" + msgResult.getErrcode() + ", ������Ϣ=" + msgResult.getErrmsg());
            }
        } else {
            System.out.println("�޷���ȡ����");
        }
        return false;
    }

    public static void main(String[] args) {
		JPushUtils o = JPushUtils.getSharedInstance();
		System.out.println(o.appKey);
		System.out.println(o.masterSecret);
        System.out.println(o.getRandomSendNo());
        o.sendAll("��Բ�˷�", "���, �ϼ�!");
        //o.sendByAlias("��Բ�˷�", "���, �ϼ�!","");
    }

    /**
     * ���� sendNo ��Ψһ�����б�Ҫ��
     * It is very important to keep sendNo unique.
     * @return sendNo
     */
    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = MAX/3;

    /**
     * ���� sendNo ��Ψһ�����б�Ҫ��
     * It is very important to keep sendNo unique.
     * @return sendNo
     */
    public static int getRandomSendNo() {
        return (int) (MIN + Math.random() * (MAX - MIN));
    }
}
