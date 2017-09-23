package cn.jpush.example;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushClientExample {
    //�ڼ���ע���ϴ�Ӧ�õ� appKey �� masterSecret
	private static  String appKey ="ab41cd22bd8a22eab33f9b81";////�������466f7032ac604e02fb7bda89
	
	private static  String masterSecret = "73f09c93214f07f9cca41147";//���ÿ��Ӧ�ö���Ӧһ��masterSecret
	
	private static JPushClient jpush = null;

	/*
	 * �������ߵ�ʱ������Ϊ��λ�����֧��10�죨864000�룩��
	 * 0 ��ʾ����Ϣ���������ߡ������û��������Ϸ�������ǰ�������û��������յ�����Ϣ��
	 * �˲������������ʾĬ�ϣ�Ĭ��Ϊ����1���������Ϣ��86400��
	 */
	private static long timeToLive =  60 * 60 * 24;  

	public static void main(String[] args) {
		/*
		 * Example1: ��ʼ��,Ĭ�Ϸ��͸�android��ios��ͬʱ����������Ϣ���ʱ��
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive);
		 * 
		 * Example2: ֻ���͸�android
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 * 
		 * Example3: ֻ���͸�IOS
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.IOS);
		 * 
		 * Example4: ֻ���͸�android,ͬʱ����������Ϣ���ʱ��
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive, DeviceEnum.Android);
		 */
 	//	jpush = new JPushClient(masterSecret, appKey, timeToLive);
		/* 
		 * �Ƿ�����ssl��ȫ����, ��ѡ
		 * ����������true�� ����false��Ĭ��Ϊ��ssl����
		 */
		//jpush.setEnableSSL(true);

		//���Է�����Ϣ����֪ͨ
		testSend("�Ǻ�","��Һ���������");
	}

	public  static void testSend(String title,String content) {
	
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	  
		// ��ʵ��ҵ���У����� sendNo ��һ�����Լ���ҵ����Դ����һ���������֡�
	    // ������Ҫ���ǣ���ȷ����Ҫ�ظ�ʹ�á�������ο� API �ĵ����˵����
	    Integer num= getRandomSendNo();
	    String sendNo=""+num;
		String msgTitle = title;
		String msgContent = content;
		
		/*
		 * IOS�豸��չ����,
		 * ����badge����������
		 */

		Map<String, Object> extra = new HashMap<String, Object>();
		IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
		iosExtra.setContent(msgContent);
		iosExtra.setTitle(msgTitle);
		extra.put("ios", iosExtra);
	//	extra.put("alter",gson.toJson(map));
		extra.put("title","I am extra infomation");  
	
		
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("title", title);
		map.put("body", msgContent);
		Gson gson=new Gson();
		//msgContent=gson.toJson(map);
		//IOS�Ͱ�׿һ��
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 1, extra);

		//�������û�����֪ͨ, ���෽����ο��ĵ�
	//	MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			System.out.println("��������������: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("���ͳɹ��� sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("����ʧ�ܣ� �������=" + msgResult.getErrcode() + ", ������Ϣ=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("�޷���ȡ����");
		}
	}
    public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX/2;
	
	/**
	 * ���� sendNo ��Ψһ�����б�Ҫ��
	 * It is very important to keep sendNo unique.
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
	    return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
