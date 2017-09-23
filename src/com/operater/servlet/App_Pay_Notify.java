package com.operater.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.DB;
import DB.GetConnection;
import Tool.MD5key;
import Tool.String_Tool;

@WebServlet(description = "�ֻ�app֧���ɹ���֪ͨ�ӿ�", urlPatterns = { "/App_Pay_Notify" })
public class App_Pay_Notify extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getSpuerConn();
		PreparedStatement past = null;
		try {
			int a=0;
			String Pay_way="";
			String Pay_wayId=request.getParameter("Pay_wayId"); //·��ֻ��Ǯ��֧��
			if(Pay_wayId.equals("1")){
				Pay_way="֧����";
			}
			else if(Pay_wayId.equals("2")){
				Pay_way="΢��";
			}
			else if(Pay_wayId.equals("3")){
				Pay_way="Ǯ��֧��";
			}
			String out_trade_no = request.getParameter("out_trade_no");// ������
			String UserNo = request.getParameter("UserNo"); // ���֧�����˺Ŷ�Ӧ��֧����Ψһ�û��š���2088��ͷ�Ĵ�16λ����
			String buyer_pay_amount = request.getParameter("buyer_pay_amount");// ������
			String encryption=request.getParameter("Signature");//����MD5  (out_trade_no+UserNo+buyer_pay_amount+"warelucent")
			System.out.println("�ֻ�֪ͨ");
			System.out.println(Pay_way);
			System.out.println(out_trade_no);
			System.out.println(UserNo);
			System.out.println(buyer_pay_amount);
			System.out.println(encryption);
            if(MD5key.getMD5Pass(out_trade_no+UserNo+buyer_pay_amount+"warelucent").equals(encryption)){
            	past = conn.prepareStatement("update  dbo.Order_Table set Pay_state = '1',Pay_wayId=? where cSheetno=? ");
            	past.setString(1, Pay_wayId);
    			past.setString(2, out_trade_no);
    			a = past.executeUpdate();
    			if (a == 1) {
    				PreparedStatement past1 = conn.prepareStatement("INSERT into App_Pay_log (datetime,cSheetno,buyer_id,buyer_pay_amount,Pay_way) values (?,?,?,?,?) ");
    				past1.setString(1, String_Tool.DataBaseTime());
    				past1.setString(2, out_trade_no);
    				past1.setString(3, UserNo);
    				past1.setString(4, buyer_pay_amount);
    				past1.setString(5, Pay_way);
    				past1.execute();
    				DB.closePreparedStatement(past1);
    			}
    			out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":\"" + a + "\"}");
            }
            else{
            	out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "�����ü��ܲ���" + "\"}");
            }
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":\"" + 0 + "\"}");
			e.printStackTrace();
		} finally {
			DB.closeStatement_Rs(past, conn);
		}
	}
}
