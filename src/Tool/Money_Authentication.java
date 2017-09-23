package Tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DB.DB;
import ModelRas.MD5key;

public class Money_Authentication {

	public static boolean Authentication_Money(Connection conn, String Buyer_id) {   //��֤Ǯ������Կ����ȷ��
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select WMoney,Money_MD5 from W_bBuyer where Buyer_id=? ");
			past.setString(1, Buyer_id);
			rs = past.executeQuery();
			if (rs.next()) {
				String WMoney = rs.getString("WMoney");
				String Money_MD5 = rs.getString("Money_MD5");
				System.out.println("ȡ������Կ:"+Money_MD5);
				System.out.println(Buyer_id);
				System.out.println(WMoney);
				System.out.println(MD5_Key_String.MD5_KEY);
				String str = MD5key.getMD5Pass(Buyer_id + WMoney+ MD5_Key_String.MD5_KEY);
			
				System.out.println("�����ɵ���Կ:"+str);
				
				System.out.println("�����ɵ���Կ:"+str.equals(Money_MD5));
				
				if (str.equals(Money_MD5)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}

	public static int Updata_Money_Key(Connection conn, String Buyer_id) {  //����Ǯ������Կ
		PreparedStatement pare20 = null;
		PreparedStatement pare21 = null;
		ResultSet rs20 = null;
		try {
			String Out_WMoney = null;
			pare20 = conn
					.prepareStatement("select WMoney from W_bBuyer where Buyer_id=?");
			pare20.setString(1, Buyer_id); // Ҫת����Buyer_id;
			rs20 = pare20.executeQuery();
			if (rs20.next()) {
				Out_WMoney = rs20.getString("WMoney");// ���Ž��а��û���id��Ǯ����
			}
			pare21 = conn
					.prepareStatement("UPDATE W_bBuyer set Money_MD5=? where Buyer_id =?");
			pare21.setString(
					1,
					MD5key.getMD5Pass(Buyer_id + Out_WMoney
							+ MD5_Key_String.MD5_KEY));
			pare21.setString(2, Buyer_id);
			int a=pare21.executeUpdate();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs20);
			DB.closePreparedStatement(pare20);
			DB.closePreparedStatement(pare21);
		}
		return 0;
	}
	
	
	public static boolean Authen_offline_MoneyCar(Connection conn, String ID_Card) {   //��֤���´�ֵ����Կ����ȷ��
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select Tel,ID_Card,Money,Money_MD5 from Off_Line_MoneyCard where ID_Card=?");
			past.setString(1, ID_Card);
			rs = past.executeQuery();
			if (rs.next()) {
				String Tel = rs.getString("Tel");
				String iD_Card = rs.getString("ID_Card");
				String Money = rs.getString("Money");
				String Money_MD5 = rs.getString("Money_MD5");
				System.out.println(Money_MD5);
				String str = MD5key.getMD5Pass(Tel + iD_Card +Money+ MD5_Key_String.MD5_KEY);
				System.out.println(str);
				System.out.println(str.equals(Money_MD5));
				if (str.equals(Money_MD5)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}
	
	public static int Updata_offline_MoneyCar(Connection conn, String ID_Card) {  //���´�ֵ������Կ
		PreparedStatement pare20 = null;
		PreparedStatement pare21 = null;
		ResultSet rs20 = null;
		try {
			String Money = null;
			String Tel=null;
			pare20 = conn
					.prepareStatement("select Tel,Money from Off_Line_MoneyCard where ID_Card=?");
			pare20.setString(1, ID_Card); // Ҫת����Buyer_id;
			rs20 = pare20.executeQuery();
			if (rs20.next()) {
				Tel = rs20.getString("Tel");// ���Ž��а��û���id��Ǯ����
				Money=rs20.getString("Money");
			}
			pare21 = conn
					.prepareStatement("UPDATE Off_Line_MoneyCard set Money_MD5=? where ID_Card =?");
			pare21.setString(
					1,
					MD5key.getMD5Pass(Tel +ID_Card+ Money+ MD5_Key_String.MD5_KEY));
			pare21.setString(2, ID_Card);
			int a=pare21.executeUpdate();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs20);
			DB.closePreparedStatement(pare20);
			DB.closePreparedStatement(pare21);
		}
		return 0;
	}
	//���ת�˼�¼
	public static int add_moneycar_wallet_log(Connection conn,String money,String fage,String Buyer_id,String ID_Card,String app_system){
		PreparedStatement past = null;
		try{
		past = conn
				.prepareStatement("INSERT INTO Moneycard_Wallet_Transfer(Move_money,date_time,Money_Into,Buyer_id,ID_Card,app_system,beizhu1)values(?,?,?,?,?,?,?)");
		past.setString(1, money);
		past.setString(2, String_Tool.DataBaseTime());
		past.setString(3, fage);//  0��ʾ��Ǯ����Ǯת����ֵ��     1��ʾ�Ѵ�ֵ����Ǯת��Ǯ��
		past.setString(4, Buyer_id); // Ǯ���˺�
		past.setString(5, ID_Card); // ��ֵ����
		past.setString(6, app_system);
		if(fage.equals("0")){
			past.setString(7, "Ǯ����Ǯת����ֵ��");
		}
		else{
			past.setString(7, "��ֵ����Ǯת��Ǯ��");
		}
		
		int count = past.executeUpdate();
		System.out.println("Ӱ����к�"+count);
		return count;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DB.closePreparedStatement(past);
		}
		return 0;
	}
	
	public static void main(String args[]){
		
		System.out.println("MD593ee260b51bcd431038389bc9d242c53".equals("MD593ee260b51bcd431038389bc9d242c53"));
	}
}
