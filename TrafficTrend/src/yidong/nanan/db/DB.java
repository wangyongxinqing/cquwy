package yidong.nanan.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DB {
	private Connection conn = null; 
	private Statement ps = null;
	private ResultSet rs = null;
	private PreparedStatement prs = null;
	public static long num = 0; 


	/**
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void execute(String sql) throws SQLException {
		try {
			conn = DaoUtils.getConnection();
			ps = conn.createStatement();
			System.out.println("" + sql);
			ps.execute(sql);
			ps.close();
			num++;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage() + "sqlΪ:" + sql);
		} finally {
			clean();
		}
	}

	public void executeBatch(List<String> sqlList, List<Object[]> paramsList)
			throws SQLException {
		if(sqlList==null){
			throw new SQLException("");
		}
		if (paramsList == null) {
			execute(sqlList);
			return;
		}
		if (paramsList.size() == 0) {
			execute(sqlList);
			return;
		}
		if(paramsList.size()!=sqlList.size()){
			throw new SQLException("");
		}
		try {
			conn=DaoUtils.getConnection();
			conn.setAutoCommit(false);
			for(int sqlInd=0;sqlInd<sqlList.size();sqlInd++){
				prs = conn.prepareStatement(sqlList.get(sqlInd));
				Object[] params = paramsList.get(sqlInd);
				for (int i = 0; i < params.length; i++) 
					prs.setObject(i + 1, params[i]);
				prs.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException(e.getMessage());
		} finally {
			this.clean();
		}
	}
	public void execute(List<String> sqlList) throws SQLException {
		String sql = "";
		try {
			conn=DaoUtils.getConnection();
			conn.setAutoCommit(false);
			ps = conn.createStatement();
			Iterator<String> it = sqlList.iterator();
			while (it.hasNext()) {
				sql = it.next();
				ps.executeUpdate(sql);
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException(e.getMessage() + "" + sql);
		} finally {
			this.clean();
		}
	}
	
	public boolean executeQuery(String sql) throws SQLException {
		try {
			conn = DaoUtils.getConnection();
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			throw new SQLException("数据查询出错" + e.getMessage() + "出错sql为：" + sql);
		} finally {
			this.clean();
		}
		return false;
	}
	
	public String executeQueryForStringResult(String sql) {
		try {
			conn = DaoUtils.getConnection();
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.clean();
		}
		return "0";
	}
	
	public ArrayList getQueryColumnName(String sql) {
		ArrayList list = new ArrayList();
		try {
			conn = DaoUtils.getConnection();
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				list.add(rs.getMetaData().getColumnName(i + 1));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			this.clean();
		}
		return list;
	}
	
	public List<String> returnList(String sql){
		List<String> list=null;
		try {
			list = new ArrayList<String>();
			conn = DaoUtils.getConnection();
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			while(rs.next()){
				String result=rs.getString(1);
				if(result==null || "".equals(result))
					list.add("0");
				else 
					list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.clean();
		}
		return list;
	}

	public void rollback() {
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void clean() {
		if (this.prs != null) {
			try {
				this.prs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.prs = null;
			}
		}
		if (this.rs != null) {
			try {
				this.rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.rs = null;
			}
		}
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.ps = null;
			}
		}
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conn = null;
			}
		}
	}
}
