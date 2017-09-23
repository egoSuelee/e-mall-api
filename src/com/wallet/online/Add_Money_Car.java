package com.wallet.online;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import DB.GetConnection;
import DB.Wallet_OthereUpda;


@WebServlet(description = "��Ӵ�ֵ��", urlPatterns = { "/Add_Money_Car" })
public class Add_Money_Car extends HttpServlet {
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
			String Buyer_id=obj.getString("Buyer_id");
		    int fage = Wallet_OthereUpda.Add_Money_Car(GetConnection.getStoreWalletConn(),Tel, ID_Card,Buyer_id);
		    out.print("{\"resultStatus\":\"" +fage+"\"}");  //1�ǳɹ�,0��ʧ��,2�ǿ��Ų�ƥ��
		} catch (Exception e) {
			 out.print("{\"resultStatus\":\"" +-1+"\"}");  //�����쳣,�������쳣
			e.printStackTrace();
		}// ���Ǯ���������
		out.flush();
		out.close();
	}

}
