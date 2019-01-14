/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.model.UserModel;

/**
 * @desciption 用户信息表操作
 * @author jack_fan
 * @date 2018年12月21日
 */
public class UserMapper {
	private static final CRPlugin log = new CRPlugin();

	/**
	 * @desciption add user to database
	 * @author jack_fan
	 * @date 2018年12月18日
	 * @param model review model
	 * @return primary key
	 */
	public Integer insert(UserModel model) {
		// 插入数据的sql语句
		String sql = "Insert Into t_user "
				+ "(name, english_name, position, gitlab_relative_id, create_time) "
				+ "Values (?,?,?,?,?)";
		PreparedStatement pst = MySQLHelper.getPreparedStatementAndKey(sql);
		if (pst == null) {
			return null;
		}
		try {
			pst.setString(1, model.getName());
			pst.setString(2, model.getEnglishName());
			pst.setString(3, model.getPosition());
			pst.setInt(4, model.getGitlabRelativeId());
			if (model.getCreateTime() != null) {
				Timestamp timeStamp = new Timestamp(model.getCreateTime().getTime());
				pst.setTimestamp(5, timeStamp);
			}
			// 插入数据操作
			int count = pst.executeUpdate();
			if (count > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if(rs.next()){
					log.log("插入返回主键为：" + rs.getInt(1));
					return rs.getInt(1);
				}
				rs.close();//释放资源
			}
		} catch (SQLException e) {
			log.log("insert data error:", e);
		}
		return null;
	}

	/**
	 * @desciption update user to database
	 * @author jack_fan
	 * @date 2018年12月18日
	 * @param model review model
	 * @return is operate success
	 */
	public boolean update(UserModel model) {
		// 插入数据的sql语句
		String sql = "update t_user set name=?, english_name=?, position=?, gitlab_relative_id=? where id=?";
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return false;
		}
		try {
			pst.setString(1, model.getName());
			pst.setString(2, model.getEnglishName());
			pst.setString(3, model.getPosition());
			pst.setInt(4, model.getGitlabRelativeId());
			pst.setInt(5, model.getId());
			// 插入数据操作
			int count = pst.executeUpdate();
			if (count > 0) {
				return true;
			}
		} catch (SQLException e) {
			log.log("insert data error:", e);
		}
		return false;
	}

	/**
	 * @desciption 通过姓名查询用户信息
	 * @author jack_fan
	 * @date 2018年12月21日
	 * @param name 转入用户名称或者空，空置则查询全部
	 * @return 用户信息
	 */
	public List<UserModel> query(String name) {
		List<UserModel> result = new ArrayList<>();
		// 插入数据的sql语句
		String sql = "select * from t_user";
		if (name != null) {
			sql += " where name=" + name;
		}
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return result;
		}
		try {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UserModel model = new UserModel();
				model.setId(rs.getInt("id"));
				model.setName(rs.getString("name"));
				model.setEnglishName(rs.getString("english_name"));
				model.setPosition(rs.getString("position"));
				model.setGitlabRelativeId(rs.getInt("gitlab_relative_id"));
				model.setCreateTime(rs.getTimestamp("create_time"));

				// log.log("query useer result:" + model);
				result.add(model);
			}
		} catch (SQLException e) {
			log.log("query data error:", e);
		}
		return result;
	}

	/**
	 * @desciption 通过user id获取相应的用户信息
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param id 用id
	 * @return 用户数据结构
	 */
	public UserModel queryById(Integer id) {
		// 插入数据的sql语句
		String sql = "select * from t_user";
		sql += " where id=" + id;
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return null;
		}
		try {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UserModel model = new UserModel();
				model.setId(rs.getInt("id"));
				model.setName(rs.getString("name"));
				model.setEnglishName(rs.getString("english_name"));
				model.setPosition(rs.getString("position"));
				model.setGitlabRelativeId(rs.getInt("gitlab_relative_id"));
				model.setCreateTime(rs.getTimestamp("create_time"));

				log.log("query useer result:" + model);
				return model;
			}
		} catch (SQLException e) {
			log.log("query data error:", e);
		}
		return null;
	}
}
