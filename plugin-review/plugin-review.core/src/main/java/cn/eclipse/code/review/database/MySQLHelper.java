/**
 * @desciption
 * mysql connection and close
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.model.MySQLConnectionModel;
import cn.eclipse.code.review.ui.preferences.store.Store;

/**
 * @desciption mysql操作
 * @author jack_fan
 * @date 2018年12月18日
 */
public class MySQLHelper {
	private static final CRPlugin log = new CRPlugin();
	private static Connection conn = null;
	private static PreparedStatement pst = null;

	/**
	 * @desciption query db by the mysql
	 * @author jack_fan
	 * @date 2018年12月18日
	 */
	private static void init() {
		if (conn == null) {
			MySQLConnectionModel model = (MySQLConnectionModel) Store.getStoreInfo(StoreType.MYSQL);
			if (model == null || model.getAddr() == null) {
				// tips error
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "错误提示", "数据无法写入数据库，请设置数据库信息！");
			}
			log.log("MySQL connection info:" + model.getAddr() + " " 
					+ model.getUsername() + " " + model.getPassword());
			try {
				// mysql connection 6.x can not to load driver
				Class.forName(model.getDirver()).newInstance();
				conn = DriverManager.getConnection(model.getAddr(), 
						model.getUsername(), model.getPassword());
			} catch (Exception e) {
				log.log("连接数据库异常：", e);
			}
		}
	}

	/**
	 * @desciption query error
	 * @author jack_fan
	 * @date 2018年12月18日
	 * @param sql
	 * @return query handle
	 */
	public static PreparedStatement getPreparedStatement(String sql) {
		init();
		if (conn == null) {
			log.log("prepareStatement is null!");
			return null;
		}
		try {
			// query handle
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			log.log("prepareStatement异常：", e);
		}
		return pst;
	}

	/**
	 * @desciption 设置返回主键
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param sql sql语句
	 * @return PreparedStatement句柄
	 */
	public static PreparedStatement getPreparedStatementAndKey(String sql) {
		init();
		if (conn == null) {
			log.log("prepareStatement is null!");
			return null;
		}
		try {
			// query handle
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			log.log("prepareStatement异常：", e);
		}
		return pst;
	}

	/**
	 * @desciption 关闭数据库连接
	 * @author jack_fan
	 * @date 2018年12月18日
	 */
	public static void close() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (pst != null) {
				pst.close();
			}
		} catch (SQLException e) {
			log.log("关闭数据库异常：", e);
		}
	}

}
