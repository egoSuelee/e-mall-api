package com.wallet.online;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cloopen.rest.sdk.CCPRestSDK;

import DB.GetConnection;
import DB.Wallet_OthereUpda;

@WebServlet(description = "IDcard��֤", urlPatterns = { "/Get_IDCard_Auth_code" })
public class Get_IDCard_Auth_code extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8"); 
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONObject obj = new JSONObject(data);
			String Tel=obj.getString("Tel");
			String ID_Card=obj.getString("ID_Card");
		    int fage = Wallet_OthereUpda.Select_Money_Car(GetConnection.getStoreWalletConn(),Tel, ID_Card);
		    System.out.println("��ʱ�õ�����"+fage);
		    if(fage==1){
		    	HashMap<String, Object> result = null;

				CCPRestSDK restAPI = new CCPRestSDK();
				restAPI.init("app.cloopen.com", "8883");// ��ʼ����������ַ�Ͷ˿ڣ���ʽ���£���������ַ����Ҫдhttps://
				restAPI.setAccount("8a216da85660607901566d0e77d4061c", "aad5b6cf1a134e74aaaba45f72b550c7");// ��ʼ�����ʺź����ʺ�TOKEN
				restAPI.setAppId("8a216da85660607901566d0e78300622");// ��ʼ��Ӧ��ID
				
				int str=(int)((Math.random()+1)*1000);
				
				result = restAPI.sendTemplateSMS(Tel,"109213" ,new String[]{""+str,"5����"});

				out.print("{\"resultStatus\":\"" + result.get("statusCode")
						+ "\"," + "\"data\":\"" + str + "\"}");
		    }
		    else if(fage==3){
		    	out.print("{\"resultStatus\":\"" + 3 + "\"," + "\"data\":" + "\"\""
						+ "}"); // �˿��Ѿ������
		    }
		    else{
		    	out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + "\"\""
						+ "}"); // �˿���û�ж�Ӧ���ֻ���
		    }
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1+ "\"," + "\"data\":" + "\"\""
					+ "}"); // �˿���û�ж�Ӧ���ֻ���
			e.printStackTrace();
		}// ���Ǯ���������
		out.flush();
		out.close();
	
	}
}
