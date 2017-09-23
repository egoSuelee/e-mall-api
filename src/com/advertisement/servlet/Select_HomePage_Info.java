package com.advertisement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.DB;
import DB.DB_goods_operate;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet("/Select_HomePage_Info") // ��ҳ������Ϣ
public class Select_HomePage_Info extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String cStoreNo = request.getParameter("cStoreNo");
		// 1 ��ȡ������
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getPos_ManagermainConn();
		Connection conn2 = GetConnection.getSpuerConn();
		PreparedStatement past = null;
		PreparedStatement carouselPast = null;
		PreparedStatement dailyHotGoodPast = null;
		ResultSet rs = null;
		ResultSet carouselRS = null;

		PreparedStatement saleGoodsPast = null;
		try {
			HashMap<String, JSONArray> resultDic = new HashMap<String, JSONArray>();
			HashMap<String, JSONArray> carouselDic = new HashMap<String, JSONArray>();
			HashMap<String, JSONArray> salesGoodsDic = new HashMap<String, JSONArray>();
			
			
			// �ֲ�ͼ���ݻ�ȡ
			carouselPast = conn2.prepareStatement("select cGoodsName,cGoodsNo,cGoodsImagePath,describe,Url,is_goods,bfresh as bFresh from dbo.Advertisement ");
			carouselRS = carouselPast.executeQuery();//�����
			carouselDic.put("carousel", ResultSet_To_JSON.resultSetToJsonArray(carouselRS));//���
			String carouselJSONString = ResultSet_To_JSON.Map_To_JSONString(carouselDic);
			
			
			//ÿ�ձ������ݻ�ȡ
			past = conn.prepareStatement("select cGroupTypeNo,cGroupTypeName ,cHomeAlias from dbo.T_GroupType where cParentNo='--' and bOnLine='1' and cStoreNo = ?");
			past.setString(1, cStoreNo);
			rs = past.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			dailyHotGoodPast = conn2.prepareStatement("select top 6 cGoodsNo, cGoodsName, bFresh, fNormalPrice, fVipPrice, bOnLine_Price , cUnit, cSpec, cGoodsImagePath, Show_Level from dbo.Daily_HotGoods where goodsType = '0' and cStoreNo = ? order by Show_Level DESC");//0��ÿ�ձ���,1��ÿ���ؼ�
			dailyHotGoodPast.setString(1, cStoreNo);
			ResultSet dailyHotGoodRS  = dailyHotGoodPast.executeQuery();
			saleGoodsPast = conn2.prepareStatement("select top 6 cGoodsNo, cGoodsName, bFresh, fNormalPrice, fVipPrice, bOnLine_Price , cUnit, cSpec, cGoodsImagePath, Show_Level, describle, saleGoodsImagePath from dbo.Daily_HotGoods where isEnabled = '1' and goodsType = '1' and cStoreNo = ? order by Show_Level DESC");
			saleGoodsPast.setString(1, cStoreNo);
			ResultSet saleGoodsRS = saleGoodsPast.executeQuery();


			resultDic.put("ÿ�ձ���", ResultSet_To_JSON.resultSetToJsonArray(dailyHotGoodRS));

			salesGoodsDic.put("������Ʒ", ResultSet_To_JSON.resultSetToJsonArray(saleGoodsRS));

			String salesGoodsJSONString = ResultSet_To_JSON.Map_To_JSONString(salesGoodsDic);
			DB.closeResultSet(dailyHotGoodRS);
			DB.closeResultSet(saleGoodsRS);

			// ����ResultSet�е�ÿ������, ��ȡÿ����������Ҫ��ʾ������
			while (rs.next()) {
				String cGroupTypeNo = rs.getString(metaData.getColumnLabel(1));
				String cGroupTypeName = rs.getString(metaData.getColumnLabel(2));
				String cHomeAlias = rs.getString(metaData.getColumnLabel(3));

				System.out.print(cHomeAlias);

				if (String_Tool.isEmpty(cGroupTypeNo)) {
					cGroupTypeNo = "";
				}
				if (String_Tool.isEmpty(cHomeAlias)) {
					cHomeAlias = cGroupTypeName;
				}
				JSONArray array = DB_goods_operate.getHighShowLevelGoodsWithGroupTypeNo(GetConnection.getPos_ManagermainConn(), cGroupTypeNo, cStoreNo);
				if (array != null && array.length() > 0) {
					resultDic.put(cHomeAlias, array);
				}
			}
			String jsonStr = ResultSet_To_JSON.Map_To_JSONStringWithTypeKeyAndValue(resultDic);
			if (resultDic != null && resultDic.size() > 0) {
				String outString = "{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":{" + "\"listsGoods\":"+ jsonStr + ","+ carouselJSONString +","+ salesGoodsJSONString +"}}";
				System.out.println(outString);
				out.print(outString);
			} else {
				String outString = "{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":{" + "\"listsGoods\":"+ jsonStr + ","+ carouselJSONString +","+ salesGoodsJSONString +"}}";
				out.print(outString);
				System.out.println(outString);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeResultSet(carouselRS);
			DB.closePreparedStatement(past);
			DB.closePreparedStatement(carouselPast);
			DB.closePreparedStatement(dailyHotGoodPast);
			DB.closeConn(conn);
			DB.closeConn(conn2);
		}
		out.flush();
		out.close();
	}
}
