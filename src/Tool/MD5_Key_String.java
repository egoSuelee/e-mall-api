package Tool;

import ModelRas.MD5key;

public class MD5_Key_String {
	
	public static String MD5_KEY="warelucent"; //����ͼ�ǩƴ�ӵ��ַ���һ��MD5
	
	//public static String MD5_KEY=new ReadConfig().getprop().getProperty("key");
	//Ǯ���û����,Ǯ��,��LUFAWANJIA;
	//�����Լ��޸�
	
	public static void main(String args[]){
		// MD5_KEY=new ReadConfig().getprop().getProperty("key");
		System.out.println(MD5_KEY);
		
		String str = MD5key.getMD5Pass("15510578956"+"69.7000"+ MD5_Key_String.MD5_KEY);
		System.out.println(str);
	}
	
}

   