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
import Tool.Money_Authentication;

@WebServlet(description = "תǮ����ֵ��", urlPatterns = { "/Move_Wallet_money_to_Car" })
public class Move_Wallet_money_to_Car extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONObject obj = new JSONObject(data);
	
			String Buyer_id = obj.getString("Buyer_id");
			String ID_Card = obj.getString("ID_Card");
			String money = obj.getString("money");
			String PayPwd = obj.getString("PayPwd");
			String app_system = obj.getString("app_system");
			if (Money_Authentication.Authentication_Money(GetConnection.getStoreWalletConn(), Buyer_id)) {
				//if (Money_Authentication.Authen_offline_MoneyCar(GetStoreWalletConn.getStoreWalletconn(cOSS_No),ID_Card)) 
				if (true)  //��֤���ߴ�ֵ��,���µĴ�ֵ��Ĭ��λ1
				{
					int fage = Wallet_OthereUpda.Move_Wallet_Money_to_car(
							GetConnection.getStoreWalletConn(),
							Buyer_id, ID_Card, money, PayPwd, app_system);
					out.print("{\"resultStatus\":\"" + fage + "\"}"); // 1�ǳɹ�,0���쳣,
																		//
																		//-4-û���ҵ��տ���
																		// 3-����Ϣ����,����û�д˿�
																		// 2
																		// -����
					System.out.println(fage);
					System.out.println("daoci");
			} else {
//					out.print("{\"resultStatus\":\"" + -3 + "\"}"); // -3��ֵ����Ϣ��۸�,
				}
			} else {
				out.print("{\"resultStatus\":\"" + -2 + "\"}"); // -2Ǯ����Ϣ��۸�,
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"}"); // �����쳣,�������쳣
			e.printStackTrace();
		}// ���Ǯ���������
		out.flush();
		out.close();
	}

}
