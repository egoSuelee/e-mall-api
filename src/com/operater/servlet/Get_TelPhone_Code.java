package com.operater.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cloopen.rest.sdk.CCPRestSDK;
import DB.DB_user_operater;
import DB.GetConnection;

public class Get_TelPhone_Code extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
	
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		doPost(request, response);
		out.flush();
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String tel=request.getParameter("Tel");
			String reg_or_update=request.getParameter("reg_or_update"); 	 // 0��ע������֤��,1���޸Ļ����֤��
			System.out.println(tel);
			System.err.println(reg_or_update);
			if (reg_or_update.equals("0")) { // ע��
				if (DB_user_operater.User_Has_Tel(GetConnection.getSpuerConn(), tel)) { // �Ѿ��д��û�
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + ""+ "\"}"); // 0�Ǵ��ֻ����Ѿ�ע��,
					System.out.println("{\"resultStatus\":" + 0 + "," + "\"dDate\":\"" + ""+ "\"}");
				}
				else {
					HashMap<String, Object> result = null;
					CCPRestSDK restAPI = new CCPRestSDK();
					restAPI.init("app.cloopen.com", "8883");// ��ʼ����������ַ�Ͷ˿ڣ���ʽ���£���������ַ����Ҫдhttps://
					restAPI.setAccount("8a216da85660607901566d0e77d4061c","aad5b6cf1a134e74aaaba45f72b550c7");// ��ʼ�����ʺź����ʺ�TOKEN
					restAPI.setAppId("8a216da85660607901566d0e78300622");// ��ʼ��Ӧ��ID
					int str = (int) ((Math.random() + 1) * 1000);
					result = restAPI.sendTemplateSMS(tel, "109213", new String[] {"" + str, "5����" });
					// �����������data������Ϣ��map��
					out.print("{\"resultStatus\":\"" + result.get("statusCode")+ "\"," + "\"dDate\":\"" + str + "\"}");
					System.out.println("{\"resultStatus\":\""+ result.get("statusCode") + "\"," + "\"dDate\":\""+ str + "\"}");
				}
			} else if (reg_or_update.equals("1")) {   //�޸�����
				if (DB_user_operater.User_Has_Tel(GetConnection.getSpuerConn(), tel)) {
					HashMap<String, Object> result = null;
					CCPRestSDK restAPI = new CCPRestSDK();
					restAPI.init("app.cloopen.com", "8883");// ��ʼ����������ַ�Ͷ˿ڣ���ʽ���£���������ַ����Ҫдhttps://
					restAPI.setAccount("8a216da85660607901566d0e77d4061c","aad5b6cf1a134e74aaaba45f72b550c7");// ��ʼ�����ʺź����ʺ�TOKEN
					restAPI.setAppId("8a216da85660607901566d0e78300622");// ��ʼ��Ӧ��ID
					int str = (int) ((Math.random() + 1) * 1000);
					result = restAPI.sendTemplateSMS(tel, "109213", new String[] {""+str, "5����" });

					System.out.println("������=" + result.get("statusCode") +" ������Ϣ= "+result.get("statusMsg"));
					out.print("{\"resultStatus\":\"" + result.get("statusCode")
							+ "\"," + "\"data\":\"" + str + "\"}");
				} else {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "\"\""
							+ "}"); // 0��û�д��û�,û�д��ֻ���
				}
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":" + -1 + "}");
		}
	}
}
