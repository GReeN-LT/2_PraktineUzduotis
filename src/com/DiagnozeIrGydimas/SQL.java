package com.DiagnozeIrGydimas;

import java.sql.*;

public class SQL
{
	private static String CONSTRING = "jdbc:sqlserver://DESKTOP-A0C0U6M;database=LigosDiagnoze";
	private  static String usrn= "sa";
	private  static String pass = "vasara";



	public static String Select(String Fields, String table){
		String result="";
		try{
			Connection con = DriverManager.getConnection(CONSTRING,usrn,pass);

			Statement st = con.createStatement();
			//execute query
			ResultSet rs = st.executeQuery("SELECT " + Fields + " FROM " + table );
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			//process result
			while (rs.next()){
				for(int i = 1; i <= count; i++)
					result+=rs.getString(i) + " ";
				result+="\n";
			}
		}catch (Exception exc){
			exc.printStackTrace();
		}
		return result;
	}

	public static void Insert(int id1, int id2, String table){
		try{
			Connection con = DriverManager.getConnection(CONSTRING,usrn,pass);
			Statement st = con.createStatement();
			//execute query
			st.executeUpdate("INSERT  INTO " + table + "(ligosId,pacientoId) VALUES " + "(" + id1+ ","+id2 + ")" );

		}catch (Exception exc){
			exc.printStackTrace();
		}

	}
}
