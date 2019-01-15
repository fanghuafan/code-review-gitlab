package cn.eclipse.code.review.ui.dialogs.viewer;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.actions.ActionGroup;
import org.gitlab.api.models.GitlabUser;

import cn.eclipse.code.review.common.CommentType;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.gitlab.GitLabApiV4Wrapper;
import cn.eclipse.code.review.gitlab.GitLabConfig;
import cn.eclipse.code.review.gitlab.IGitLabApiWrapper;
import cn.eclipse.code.review.model.GitLabConnectionModel;
import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.repository.CodeReviewRepositoryProvider;
import cn.eclipse.code.review.ui.dialogs.CloseReviewDialog;
import cn.eclipse.code.review.ui.dialogs.CodeReviewListDailog;
import cn.eclipse.code.review.ui.dialogs.ReviewDialog;
import cn.eclipse.code.review.ui.dialogs.ViewReviewDialog;
import cn.eclipse.code.review.ui.icon.CRIcons;
import cn.eclipse.code.review.ui.preferences.store.Store;

//继承ActionGroup
public class ReviewActionGroup extends ActionGroup {
	// reviewer list
	private TableViewer tableViewer;
	private CodeReviewListDailog codeReviewListDailog;

	/**
	 * 鼠标右键有菜单,首先要 生成菜单Menu,并将两个Action传入
	 */
	public void fillContextMenu(IMenuManager mgr) {
		MenuManager menuManager = (MenuManager) mgr;
		// Skip GitLab
		menuManager.add(new SkipGitLabAction());
		// 查看code review content
		menuManager.add(new OpenAction());
		// edit comment
		menuManager.add(new EditAction());
		// close review todo list
		menuManager.add(new CloseAction());
		// refresh
		menuManager.add(new RefreshAction());

		Table table = tableViewer.getTable();
		Menu menu = menuManager.createContextMenu(table);
		table.setMenu(menu);
	}

	public ReviewActionGroup(TableViewer tableViewer, CodeReviewListDailog codeReviewListDailog) {
		this.tableViewer = tableViewer;
		this.codeReviewListDailog = codeReviewListDailog;
	}

	/**
	 * @desciption GitLab跳转
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private class SkipGitLabAction extends Action {
		public SkipGitLabAction() {
			setText("Skip GitLab");
			setImageDescriptor(CRIcons.skipIcon);
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			ReviewModel obj = (ReviewModel) (selection.getFirstElement());
			if (obj == null) {
				MessageDialog.openInformation(null, null, "请选择记录");
			} else {
				String skipUrl = handleUrl(obj);
				if (skipUrl == null) {
					MessageDialog.openInformation(null, null, "没有找到跳转链接");
					return;
				}
				URI uri = java.net.URI.create(skipUrl);
				Desktop dp=java.awt.Desktop.getDesktop();
				// 获取系统默认浏览器打开链接
				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
					try {
						dp.browse(uri);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * @desciption 获取对应的gitlab跳转地址
		 * @author jack_fan
		 * @date 2019年1月8日
		 * @param obj
		 * @return gitlab跳转地址
		 */
		private String handleUrl(ReviewModel obj) {
			GitLabConnectionModel model = (GitLabConnectionModel) Store.getStoreInfo(StoreType.GITLAB);
			if (model == null || model.getAddr() == null) {
				return null;
			}
			if (obj != null
					&& ((obj.getCommitId() != null && CommentType.COMMENT.getType().equals(obj.getCommentType()))
							|| (obj.getGitlabCommentId() != null
									&& !CommentType.COMMENT.getType().equals(obj.getCommentType())))) {
				if(CommentType.COMMENT.getType().equals(obj.getCommentType())) {
					return model.getAddr() + "/" + obj.getGitlabOwner()
							+ "/" + obj.getRepoName() + "/commit/" + obj.getCommitId();
				} else if (CommentType.ISSUE.getType().equals(obj.getCommentType())) {
					return model.getAddr() + "/" + obj.getGitlabOwner() + "/" + obj.getRepoName() + "/issues/"
							+ obj.getGitlabCommentId();
				} else if (CommentType.SNIPPETS.getType().equals(obj.getCommentType())) {
					return model.getAddr() + "/snippets/" + obj.getGitlabCommentId();
				}
			}
			return null;
		}
	}

	/**
	 * @desciption 获取用户名
	 * @author jack_fan
	 * @date 2019年1月10日
	 * @return 用户名
	 */
	@Deprecated
	private String getUserName(ReviewModel model) {
		GitLabConnectionModel store = (GitLabConnectionModel) Store.getStoreInfo(StoreType.GITLAB);
		if (store == null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误提示", "您没有设置GitLab相关信息");
			return null;
		}
		GitLabConfig config = new GitLabConfig();
		config.setGitlabUrl(store.getAddr());
		config.setPrivateToken(store.getPrivateToken());
		config.setV4(store.getIsV4());
		// 判断是否是V3版本
		IGitLabApiWrapper gApi = new GitLabApiV4Wrapper();
		gApi.setConfig(config);
		String repoName = CodeReviewRepositoryProvider.findGitRepositoryByProjectFilePath(model.getAbsoluteClassPath());
		gApi.init(repoName, CommentType.getCommentType(model.getCommentType()));
		GitlabUser user = gApi.getCurrentUserInfo();
		if (user != null) {
			return user.getUsername();
		}
		return null;
	}

	/**
	 * @desciption 查看comment/issue/snippets
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private class OpenAction extends Action {
		public OpenAction() {
			setText("Open Review");
			setImageDescriptor(CRIcons.openIcon);
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			ReviewModel obj = (ReviewModel) (selection.getFirstElement());
			if (obj == null) {
				MessageDialog.openInformation(null, null, "请选择记录");
			} else {
				ViewReviewDialog dialog = new ViewReviewDialog(Display.getCurrent().getActiveShell());
				dialog.setmReviewModel(obj);
				dialog.open();
			}
		}
	}

	/**
	 * @desciption 编辑todo list
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private class EditAction extends Action {
		public EditAction() {
			setText("Edit Review");
			setImageDescriptor(CRIcons.editIcon);
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			ReviewModel obj = (ReviewModel) (selection.getFirstElement());
			if (obj == null) {
				MessageDialog.openInformation(null, null, "请选择记录");
			} else {
				ReviewDialog dialog = new ReviewDialog(Display.getCurrent().getActiveShell());
				dialog.setmReviewModel(obj);
				dialog.setCodeReviewListDailog(codeReviewListDailog);
				dialog.open();
			}
		}
	}

	/**
	 * @desciption 关闭todo list
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private class CloseAction extends Action {
		public CloseAction() {
			setText("Close Review");
			setImageDescriptor(CRIcons.closeIcon);
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			ReviewModel obj = (ReviewModel) (selection.getFirstElement());
			if (obj == null) {
				MessageDialog.openInformation(null, null, "请选择记录");
			} else {
				CloseReviewDialog dialog = new CloseReviewDialog(Display.getCurrent().getActiveShell());
				dialog.setmReviewModel(obj);
				dialog.setCodeReviewListDailog(codeReviewListDailog);
				dialog.open();
			}
		}
	}

	/**
	 * @desciption 刷新table list
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private final class RefreshAction extends Action {
		public RefreshAction() {
			setText("refresh");
			setImageDescriptor(CRIcons.refreshIcon);
		}

		@Override
		public void run() {
			codeReviewListDailog.reviewList(null);
			tableViewer.refresh();
		}
	}
}