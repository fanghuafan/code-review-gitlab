package cn.eclipse.code.review.ui.dialogs.viewer;


import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cn.eclipse.code.review.common.CommentType;
import cn.eclipse.code.review.model.ReviewModel;

/**
 * 这个方法主要是作为"标签器"的作用来用的. 
 * "标签器"将一个个实体对象的字段变量分别取出然后系那是在TableViewer的各个列中. 实现对应的接口
 * 
 * @author jack_fan
 */
public class TableViewerLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {
	private static final String COMMENT_VIEW_HEADER = "////////////////////////////////comment/////////////////////////////";

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * 由此方法决定数据记录在表格的每一列是显示什么文字
	 * 
	 * @param element     实体类对象
	 * @param columnIndex 列号，0是第一列
	 * @return 返回值一定要避免NULL值,否则出错
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ReviewModel model = (ReviewModel) element;
		if (columnIndex == 0) {
			return model.getProject();
		}

		if (columnIndex == 1) {
			return model.getReviewer();
		}

		if (columnIndex == 2) {
			return model.getToCoder();
		}

		if (columnIndex == 3) {
			return model.getClassPath();
		}

		if (columnIndex == 4) {
			return model.getStartLine() + "/" + model.getEndLine();
		}

		if (columnIndex == 5) {
			if (model.getComment() != null) {
				String[] commentS = model.getComment().split(COMMENT_VIEW_HEADER);
				if (commentS.length > 1) {
					return commentS[1];
				}
			}

			return model.getComment();
		}

		if (columnIndex == 6) {
			return model.getStatus() == null || model.getStatus() == 1 ? "close" : "open";
		}

		if (columnIndex == 7) {
			return model.getCommentType();
		}

		if (columnIndex == 8) {
			return model.getCommentTime().toString();
		}

		return "";
	}

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof ReviewModel) {
			ReviewModel model = ((ReviewModel) element);
			if ((model.getGitlabCommentId() != null && model.getGitlabCommentId() > 0
					&& !CommentType.COMMENT.getType().equals(model.getCommentType()))
					|| (model.getCommitId() != null && CommentType.COMMENT.getType().equals(model.getCommentType()))) {
				return new Color(Display.getDefault(), 251, 255, 242);
			} else {
				return new Color(Display.getDefault(), 252, 242, 242);
			}
		}

		return super.getBackground(element);
	}
}