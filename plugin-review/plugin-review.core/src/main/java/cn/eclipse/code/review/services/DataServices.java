/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月19日
 */
package cn.eclipse.code.review.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.gitlab.api.models.CommitComment;
import org.gitlab.api.models.GitLabSnippets;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNote;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.CommentType;
import cn.eclipse.code.review.common.StatusType;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.database.CodeReviewMapper;
import cn.eclipse.code.review.database.UserMapper;
import cn.eclipse.code.review.gitlab.GitLabApiV3Wrapper;
import cn.eclipse.code.review.gitlab.GitLabApiV4Wrapper;
import cn.eclipse.code.review.gitlab.GitLabConfig;
import cn.eclipse.code.review.gitlab.IGitLabApiWrapper;
import cn.eclipse.code.review.model.GitLabConnectionModel;
import cn.eclipse.code.review.model.QueryConditionModel;
import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.model.UserModel;
import cn.eclipse.code.review.repository.CodeReviewRepositoryProvider;
import cn.eclipse.code.review.ui.preferences.store.Store;

/**
 * @desciption 数据库操作服务
 * @author jack_fan
 * @date 2018年12月19日
 */
public class DataServices {
	private static final String CODE_VIEW_HEADER = "///////////////////////////////////code////////////////////////////////";
	private static final String COMMENT_VIEW_HEADER = "////////////////////////////////comment/////////////////////////////";
	private static final String MARKDOWN_CODE_LABEL = "```````          \r\n";
	private CRPlugin log = new CRPlugin();
	// code review list
	private CodeReviewMapper mapper = new CodeReviewMapper();
	// user info
	private UserMapper userMapper = new UserMapper();
	// 默认初始化gitlan V4版本接口
	private IGitLabApiWrapper gApi = new GitLabApiV4Wrapper();

	private static final String COMMENT = "comment";
	private static final String ISSUE = "issue";
	private static final String SNIPPETS = "snippets";

	/////////////////////////////////////////// review////////////////////////////////////////////////
	/**
	 * @desciption 获取工程列表
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @return project列表
	 */
	public List<String> getPrjectList() {
		return mapper.getAllProject();
	}

	/**
	 * @desciption 获取review记录
	 * @author jack_fan
	 * @date 2018年12月20日
	 * @param condition
	 * @return review记录
	 */
	public List<ReviewModel> getReviewRecordList(QueryConditionModel condition) {
		return mapper.query(condition);
	}

	/**
	 * @desciption 计算code review数量
	 * @author jack_fan
	 * @date 2019年1月11日
	 * @param condition
	 * @return
	 */
	public long count(QueryConditionModel condition) {
		return mapper.countCodeReview(condition);
	}

	/**
	 * @desciption 插入或者更新review信息到数据库
	 * @author jack_fan
	 * @date 2018年12月19日
	 * @return 是否操作成功
	 */
	public boolean handleReviewToDB(ReviewModel model) {
		Integer resultId = gitlabDataHandle(model);
		if (!COMMENT.equalsIgnoreCase(model.getCommentType())) {
			if (resultId != null) {
				model.setGitlabCommentId(resultId);
			}
		}
		if (model.getId() == null) {
			return mapper.insert(model);
		} else {
			return mapper.updateInfo(model);
		}
	}

	/**
	 * @desciption 插入review信息
	 * @author jack_fan
	 * @date 2018年12月19日
	 * @return 是否操作成功
	 */
	public boolean updateReviewToClose(ReviewModel model) {
		// 关闭comment数据库更新status为关闭状态，gitlab issue则关闭
		if (model.getStatus() == StatusType.CLOSE.getType()) {
			if (model.getGitlabCommentId() != null) {
				closeIssue(model);
			}
			return mapper.update(model);
		}
		Integer resultId = gitlabDataHandle(model);
		if (!COMMENT.equalsIgnoreCase(model.getCommentType())) {
			if (resultId != null) {
				model.setGitlabCommentId(resultId);
			}
		}
		// 插入gitlab
		if (resultId != null) {
			return mapper.update(model);
		}
		return false;
	}

	/**
	 * @desciption 当是issue时关闭
	 * @author jack_fan
	 * @date 2019年1月7日
	 * @param model
	 * @return 是否关闭issue成功
	 */
	private boolean closeIssue(ReviewModel model) {
		if (CommentType.ISSUE.getType().equals(model.getCommentType())) {
			String commentStr = model.getComment();
			if (commentStr != null) {
				commentStr = commentStr.replace(CODE_VIEW_HEADER, MARKDOWN_CODE_LABEL);
				commentStr = commentStr.replace(COMMENT_VIEW_HEADER, MARKDOWN_CODE_LABEL);
			}
			GitLabConnectionModel store = (GitLabConnectionModel) Store.getStoreInfo(StoreType.GITLAB);
			if (store == null) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误提示", "您没有设置GitLab相关信息");
				return false;
			}
			GitLabConfig config = new GitLabConfig();
			config.setGitlabUrl(store.getAddr());
			config.setPrivateToken(store.getPrivateToken());
			config.setV4(store.getIsV4());
			// 判断是否是V3版本
			IGitLabApiWrapper gApi = new GitLabApiV4Wrapper();
			gApi.setConfig(config);
			try {
				gApi.init(model.getRepoName(), CommentType.getCommentType(model.getCommentType()));
				GitlabIssue closeIssue = gApi.createOrUpdateIssue(model.getGitlabCommentId(), null, null, "COde Review",
						commentStr, model.getClassPath() + " file code review",
						GitlabIssue.Action.CLOSE);
				if (closeIssue != null) {
					GitlabNote note = gApi.createIssueNote(closeIssue, model.getCoderReply());
					if (note != null) {
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @desciption gitlab数据处理
	 * @author jack_fan
	 * @date 2018年12月27日
	 * @param model review数据结构
	 * @return 是否处理成功
	 */
	private Integer gitlabDataHandle(ReviewModel model) {
		GitLabConnectionModel store = (GitLabConnectionModel) Store.getStoreInfo(StoreType.GITLAB);
		log.log("gitlab connect model:" + store);
		if (store == null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误提示", "您没有设置GitLab相关信息");
			return null;
		}
		GitLabConfig config = new GitLabConfig();
		config.setGitlabUrl(store.getAddr());
		config.setPrivateToken(store.getPrivateToken());
		config.setV4(store.getIsV4());
		// 判断是否是V3版本
		if (!store.getIsV4()) {
			gApi = new GitLabApiV3Wrapper();
		}
		gApi.setConfig(config);
		// 设置提交列表
		config.setCommitSHA(CodeReviewRepositoryProvider.getCommitSHAList(model.getAbsoluteClassPath()));
		String repoName = CodeReviewRepositoryProvider.findGitRepositoryByProjectFilePath(model.getAbsoluteClassPath());
		log.log("gitlab repo name:" + repoName);
		gApi.init(repoName, CommentType.getCommentType(model.getCommentType()));
		// 设置gitlab信息
		if (gApi.getGitLabProject() != null && gApi.getGitLabProject().getOwner() != null) {
			model.setGitlabProjectId(gApi.getGitLabProject().getOwner().getId());
			model.setGitlabOwner(gApi.getGitLabProject().getOwner().getUsername());
		}
		// 判断GitLab中是否更新
		return handleGitLabData(model, store.getIsV4());
	}

	/**
	 * @desciption 增加数据到GitLab
	 * @author jack_fan
	 * @date 2018年12月27日
	 * @param model
	 * @param isV4  是否是v4版本
	 * @return
	 */
	private Integer handleGitLabData(ReviewModel model, boolean isV4) {
		switch(model.getCommentType()) {
		case COMMENT:
			return handleComment(model);
		case ISSUE:
			return handleIssue(model);
		case SNIPPETS:
			return handleSnippets(model);
		}
		return null;
	}

	/**
	 * @desciption comment/issue/snippets
	 * @author jack_fan
	 * @date 2019年1月4日
	 * @param model
	 * @return
	 */
	private Integer handleComment(ReviewModel model) {
		String path = model.getAbsoluteClassPath();
		if(path == null) {
			return null;
		}
		path = path.replace("\\", "/");
		String repoName = CodeReviewRepositoryProvider.findGitRepositoryByProjectFilePath(path);
		String fullPath = null;
		String[] paths = path.split("/" + repoName + "/");
		if (paths.length < 2) {
			return null;
		}
		fullPath = paths[1];
		// 添加comment
		String revision = gApi.getRevisionForLine(new File(path), fullPath, model.getEndLine() + 1);
		model.setCommitId(revision);
		// 提交版本信息不存在
		if (revision == null) {
			return null;
		}
		String commentStr = model.getComment();
		if (commentStr != null) {
			commentStr = commentStr.replace(CODE_VIEW_HEADER, MARKDOWN_CODE_LABEL);
			commentStr = commentStr.replace(COMMENT_VIEW_HEADER, MARKDOWN_CODE_LABEL);
		}
		CommitComment cComment = gApi.createReviewComment(revision, fullPath, model.getEndLine(),
				commentStr);
		if (cComment != null) {
			// 1表示已经添加成功
			return 1;
		}
		return null;
	}

	/**
	 * @desciption 处理issue的添加与更新
	 * @author jack_fan
	 * @date 2019年1月5日
	 * @param model
	 * @return 操作是否成功
	 */
	private Integer handleIssue(ReviewModel model) {
		GitlabIssue issue = null;
		String commentStr = model.getComment();
		if (commentStr != null) {
			commentStr = commentStr.replace(CODE_VIEW_HEADER, MARKDOWN_CODE_LABEL);
			commentStr = commentStr.replace(COMMENT_VIEW_HEADER, MARKDOWN_CODE_LABEL);
		}

		try {
			issue = gApi.createOrUpdateIssue(
					model.getGitlabCommentId() == null || model.getGitlabCommentId() <= 0 ? null
							: model.getGitlabCommentId(),
					null, null, "COde Review",
					commentStr,
					model.getClassPath() + " file code review",
					GitlabIssue.Action.REOPEN);
			if (issue != null) {
				return issue.getIid();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @desciption 添加或者更新snippet
	 * @author jack_fan
	 * @date 2019年1月5日
	 * @param model
	 * @return
	 */
	public Integer handleSnippets(ReviewModel model) {
		if (model == null || model.getClassPath() == null || model.getCode() == null) {
			return null;
		}
		String fileName = model.getClassPath().substring(model.getClassPath().lastIndexOf("/") + 1,
				model.getClassPath().length());
		String commentStr = model.getComment();
		if (commentStr != null) {
			String[] comments = commentStr.split(COMMENT_VIEW_HEADER);
			if (comments != null && comments.length > 1) {
				commentStr = comments[1];
			}
		}
		try {
			GitLabSnippets snippets = gApi.createOrUpdateSippets(model.getGitlabCommentId(), model.getClassPath(),
					fileName, model.getCode(),
					commentStr, null);
			if (snippets != null) {
				return snippets.getId();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	////////////////////////////////////////////// user//////////////////////////////////////////////
	/**
	 * @desciption 获取所有的用户信息
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @return 用户信息列表
	 */
	public List<UserModel> getAllUsers() {
		return userMapper.query(null);
	}

	/**
	 * @desciption 通过用户ID获取用户信息
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param id 用ID
	 * @return 用户信息
	 */
	public UserModel getUserById(Integer id) {
		return userMapper.queryById(id);
	}

	/**
	 * @desciption 更新user信息
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param user 用户相关信息
	 * @return 是否更新成功
	 */
	public boolean updateUser(UserModel user) {
		return userMapper.update(user);
	}

	/**
	 * @desciption 插入用户信息，返回自增主键值
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param user 用户信息
	 * @return 自增主键值
	 */
	public Integer addUser(UserModel user) {
		return userMapper.insert(user);
	}

}
