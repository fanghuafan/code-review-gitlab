/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.DateUtils;
import cn.eclipse.code.review.model.QueryConditionModel;
import cn.eclipse.code.review.model.ReviewModel;

/**
 * @desciption db mapped
 * @author jack_fan
 * @date 2018年12月18日
 */
public class CodeReviewMapper {
	private static final CRPlugin log = new CRPlugin();

	/**
	 * @desciption 获取所有的工程
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @return 工程列表
	 */
	public List<String> getAllProject() {
		List<String> daList = new ArrayList<>();
		String sql = "select project from t_code_review_info group by project order by project ASC";
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return daList;
		}
		try {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				daList.add(rs.getString("project"));
			}
		} catch (SQLException e) {
			log.log("getAllProject data error:", e);
		}
		return daList;
	}

	/**
	 * @desciption add review to database
	 * @author jack_fan
	 * @date 2018年12月18日
	 * @param model review model
	 * @return is operate success
	 */
	public boolean insert(ReviewModel model) {
		// 插入数据的sql语句
		String sql = "Insert Into t_code_review_info "
				+ "(reviewer, to_coder, project, class_path, start_line, end_line, comment_time, comment, "
				+ "status, complete_time, code, code_change, comment_type, commit_id, coder_reply,"
				+ "reviewer_id,to_coder_id,review_grade,delete_status,repo_name,gitlab_owner,"
				+ "gitlab_project_id,gitlab_review_id, project_class_path,title) "
				+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = MySQLHelper.getPreparedStatementAndKey(sql);
		if (pst == null) {
			return false;
		}
		try {
			pst.setString(1, model.getReviewer());
			pst.setString(2, model.getToCoder());
			pst.setString(3, model.getProject());
			pst.setString(4, model.getClassPath());
			pst.setInt(5, model.getStartLine());
			pst.setInt(6, model.getEndLine());
			if (model.getCommentTime() != null) {
				Timestamp timeStamp = new Timestamp(model.getCommentTime().getTime());
				pst.setTimestamp(7, timeStamp);
			}
			pst.setString(8, model.getComment());
			pst.setInt(9, model.getStatus());
			if (model.getCompleteTime() != null) {
				Timestamp timeStamp = new Timestamp(model.getCompleteTime().getTime());
				pst.setTimestamp(10, timeStamp);
			} else {
				Timestamp timeStamp = new Timestamp(new Date().getTime());
				pst.setTimestamp(10, timeStamp);
			}
			pst.setString(11, model.getCode());
			pst.setInt(12, model.getCodeChange());
			pst.setString(13, model.getCommentType());
			pst.setString(14, model.getCommitId());
			pst.setString(15, model.getCoderReply());
			pst.setInt(16, model.getReviewerId());
			pst.setInt(17, model.getToCoderId());
			pst.setString(18, model.getReviewGrade());
			pst.setInt(19, model.getDeleteStatus());
			pst.setString(20, model.getRepoName());
			pst.setString(21, model.getGitlabOwner());
			pst.setInt(22, model.getGitlabProjectId() == null ? 0 : model.getGitlabProjectId());
			pst.setInt(23, model.getGitlabCommentId() == null ? 0 : model.getGitlabCommentId());
			pst.setString(24, model.getProjectClassPath());
			pst.setString(25, model.getTitle());

			int count = pst.executeUpdate();
			if (count > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					log.log("插入返回主键为：" + rs.getInt(1));
					model.setId(rs.getInt(1));
				}
				rs.close();// 释放资源

				return true;
			}
		} catch (SQLException e) {
			log.log("insert data error:", e);
		}
		return false;
	}

	/**
	 * @desciption 更新review状态
	 * @author jack_fan
	 * @date 2018年12月21日
	 * @param model
	 * @return 是否更新成功
	 */
	public boolean update(ReviewModel model) {
		// 更新数据的sql语句
		String sql = "update t_code_review_info set status=?,complete_time=?,coder_reply=? where id=?";
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return false;
		}
		try {
			pst.setInt(1, model.getStatus());
			if (model.getCompleteTime() != null) {
				Timestamp timeStamp = new Timestamp(model.getCompleteTime().getTime());
				pst.setTimestamp(2, timeStamp);
			} else {
				Timestamp timeStamp = new Timestamp(new Date().getTime());
				pst.setTimestamp(2, timeStamp);
			}
			pst.setString(3, model.getCoderReply());
			pst.setInt(4, model.getId());
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
	 * @desciption 更新review信息
	 * @author jack_fan
	 * @date 2018年12月21日
	 * @param model
	 * @return 是否更新成功
	 */
	public boolean updateInfo(ReviewModel model) {
		// 更新数据的sql语句
		String sql = "update t_code_review_info set "
				+ "comment=?,"
				+ "comment_type=?,"
				+ "to_coder=?,"
				+ "to_coder_id=?,"
				+ "delete_status=?,"
				+ "gitlab_review_id=?,"
				+ "title=?"
				+ (model.getCommitId() == null ? "" : " ,commit_id=?")
				+ (model.getGitlabOwner() == null ? "" : " ,gitlab_owner=?") 
				+ (model.getGitlabProjectId() == null ? "" : " ,gitlab_project_id=?") 
				+ " where id=?";
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return false;
		}
		try {
			pst.setString(1, model.getComment());
			pst.setString(2, model.getCommentType());
			pst.setString(3, model.getToCoder());
			pst.setInt(4, model.getToCoderId());
			pst.setInt(5, model.getDeleteStatus());
			pst.setInt(6, model.getGitlabCommentId() == null ? 0 : model.getGitlabCommentId());
			pst.setString(7, model.getTitle());

			int index = 7;
			// commit id
			if (model.getCommitId() != null) {
				pst.setString(++index, model.getCommitId());
			}
			// gitlab project owner
			if (model.getGitlabOwner() != null) {
				pst.setString(++index, model.getGitlabOwner());
			}
			if (model.getGitlabProjectId() != null) {
				pst.setInt(++index, model.getGitlabProjectId());
			}
			pst.setInt(++index, model.getId());

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
	 * @desciption 通过条件查询code review
	 * @author jack_fan
	 * @date 2018年12月19日
	 * @param condition 查询条件
	 * @return 查询结果
	 */
	public List<ReviewModel> query(QueryConditionModel condition) {
		List<ReviewModel> daList = new ArrayList<>();
		String sql = createSql(condition);
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return daList;
		}
		try {
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				ReviewModel model = new ReviewModel();
				model.setId(rs.getInt("id"));
				model.setReviewer(rs.getString("reviewer"));
				model.setToCoder(rs.getString("to_coder"));
				model.setProject(rs.getString("project"));
				model.setClassPath(rs.getString("class_path"));
				model.setStartLine(rs.getInt("start_line"));
				model.setEndLine(rs.getInt("end_line"));
				model.setCommentTime(rs.getTimestamp("comment_time"));
				model.setComment(rs.getString("comment"));
				model.setStatus(rs.getInt("status"));
				model.setCompleteTime(rs.getTimestamp("complete_time"));
				model.setCode(rs.getString("code"));
				model.setCodeChange(rs.getInt("code_change"));
				model.setCommentType(rs.getString("comment_type"));
				model.setCommitId(rs.getString("commit_id"));
				model.setCoderReply(rs.getString("coder_reply"));
				model.setReviewerId(rs.getInt("reviewer_id"));
				model.setToCoderId(rs.getInt("to_coder_id"));
				model.setReviewGrade(rs.getString("review_grade"));
				model.setDeleteStatus(rs.getInt("delete_status"));
				model.setRepoName(rs.getString("repo_name"));
				model.setGitlabProjectId(rs.getInt("gitlab_project_id"));
				model.setGitlabCommentId(rs.getInt("gitlab_review_id"));
				model.setRepoName(rs.getString("repo_name"));
				model.setGitlabOwner(rs.getString("gitlab_owner"));
				model.setProjectClassPath(rs.getString("project_class_path"));
				model.setTitle(rs.getString("title"));

//				log.log("query comment result:" + model);
				daList.add(model);
			}
		} catch (SQLException e) {
			log.log("insert data error:", e);
		}
		return daList;
	}

	/**
	 * @desciption 计算所有code review总数
	 * @author jack_fan
	 * @date 2019年1月10日
	 * @param condition
	 * @return 总数
	 */
	public long countCodeReview(QueryConditionModel condition) {
		String sql = "select count(*) as total from t_code_review_info " + whereCondition(condition);
		log.log("count sql:" + sql);
		PreparedStatement pst = MySQLHelper.getPreparedStatement(sql);
		if (pst == null) {
			return 0;
		}
		try {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				return rs.getLong("total");
			}
		} catch (SQLException e) {
			log.log("insert data error:", e);
		}
		return 0;
	}

	/**
	 * @desciption 根据条件拼接sql语句
	 * @author jack_fan
	 * @date 2018年12月19日
	 * @param condition 查询条件
	 * @return sql语句
	 */
	private String createSql(QueryConditionModel condition) {
		String sql = "select * from t_code_review_info ";
		// 判断是否有条件
		String temp = whereCondition(condition);

		// 按评论时间倒序
		temp += " order by comment_time desc";

		// 设置分页
		if (condition != null && condition.getPage() > 0 && condition.getPageSize() > 0) {
			temp += " limit " + ((condition.getPage() - 1) * condition.getPageSize()) + "," + condition.getPageSize();
		}

		temp = sql + temp;
		log.log("query sql:" + temp);
		return temp;
	}

	/**
	 * @desciption 查询过滤条件
	 * @author jack_fan
	 * @date 2019年1月10日
	 * @param condition
	 * @param temp
	 * @return where过滤条件
	 */
	private String whereCondition(QueryConditionModel condition) {
		String temp = "";
		boolean isFrist = true;
		if (condition != null) {
			// 审查人条件
			if (condition.getReviewer() != null) {
				isFrist = false;
				temp = " reviewer='" + condition.getReviewer() + "'";
			}
			// 过滤工程信息
			if (condition.getProject() != null) {
				String temp0 = " project='" + condition.getProject() + "'";
				if (!isFrist) {
					temp = temp + " and " + temp0;
				} else {
					temp = temp0;
				}
				isFrist = false;
			}
			// 被审查人条件
			if (condition.getToCoder() != null) {
				String temp0 = " to_coder='" + condition.getToCoder() + "'";
				if (!isFrist) {
					temp = temp + " and " + temp0;
				} else {
					temp = temp0;
				}
				isFrist = false;
			}
			// comment模糊查询
			if (condition.getKey() != null && !condition.getKey().trim().equals("")) {
				String temp0 = " (comment like '%" + condition.getKey() + "%' or coder_reply like '%"
						+ condition.getKey() + "%')";
				if (!isFrist) {
					temp = temp + " and " + temp0;
				} else {
					temp = temp0;
				}
				isFrist = false;
			}
			// 时间条件
			if (condition.getStartTime() != null || condition.getEndTime() != null) {
				String temp0 = null;
				if (condition.getStartTime() != null && condition.getEndTime() != null) {
					temp0 = " (comment_time >='" + DateUtils.dateTimeFormat(condition.getStartTime())
							+ "' and comment_time <= '" + DateUtils.dateTimeFormat(condition.getEndTime()) + "')";
				} else if (condition.getStartTime() != null && condition.getEndTime() == null) {
					temp0 = " comment_time >= '" + DateUtils.dateTimeFormat(condition.getStartTime()) + "'";
				} else {
					temp0 = " comment_time <= '" + DateUtils.dateTimeFormat(condition.getEndTime()) + "'";
				}
				if (!isFrist) {
					temp = temp + " and " + temp0;
				} else {
					temp = temp0;
				}
				isFrist = false;
			}
			// 判断是否拼接where
			final String nullStr = "";
			if (!nullStr.equals(temp)) {
				temp = " where " + temp;
			}
		}
		return temp;
	}

}
