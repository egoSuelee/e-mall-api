package Tool;
import java.util.HashMap;
import com.cloopen.rest.sdk.CCPRestSDK;

public class SDKTestSendTemplateSMS {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GET_Tel_SMS("18310111662");
	}
	public static HashMap<String, Object> GET_Tel_SMS(String Tel){
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// ��ʼ����������ַ�Ͷ˿ڣ���ʽ���£���������ַ����Ҫдhttps://
		restAPI.setAccount("8a216da85660607901566d0e77d4061c",
				"aad5b6cf1a134e74aaaba45f72b550c7");// ��ʼ�����ʺź����ʺ�TOKEN
		restAPI.setAppId("8a216da85660607901566d0e78300622");// ��ʼ��Ӧ��ID
		int str = (int) ((Math.random() + 1) * 1000);
		result = restAPI.sendTemplateSMS(Tel, "109213", new String[] {""+str, "5����" });
		result.put("telcode", str);
		System.out.println("������=" + result.get("statusCode") +" ������Ϣ= "+result.get("statusMsg"));
		return result;
	}

}
