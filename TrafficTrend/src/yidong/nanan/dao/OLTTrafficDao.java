package yidong.nanan.dao;

import java.sql.SQLException;
import java.util.List;

import yidong.nanan.db.DB;

public class OLTTrafficDao {
	public static void batchOLTSql(List<String> addSqlList) throws SQLException{
		DB mydb=new DB();
		mydb.execute(addSqlList);
	}
	
	public static List<String> getAllList(String sql){
		DB mydb=new DB();
		return mydb.returnList(sql);
	}
}
