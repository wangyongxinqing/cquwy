package yidong.nanan.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import yidong.nanan.db.DaoUtils;

public class PojoUtil {
	/**
	 * 把数据库查询结果封装成实体的List集合
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T>List<T> getPojoListFromDB(String sql,Class<T> clazz){
		Connection conn=null;
		try {
			conn=DaoUtils.getConnection();
			QueryRunner runner=new QueryRunner();
			return runner.query(conn, sql, new BeanListHandler<T>(clazz));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if (conn != null) {
					try {
						conn.setAutoCommit(true);
					} finally {
						conn.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
