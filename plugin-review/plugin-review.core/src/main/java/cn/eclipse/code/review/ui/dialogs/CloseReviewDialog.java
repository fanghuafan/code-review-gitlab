/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.ui.dialogs;

import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.eclipse.code.review.common.StatusType;
import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.services.DataServices;
import cn.eclipse.code.review.ui.icon.CRIcons;

/**
 * @desciption 关闭comment/issue/snippets
 * @author jack_fan
 * @date 2018年12月21日
 */
public class CloseReviewDialog extends Dialog {
	// reply text
	StyledText styledText;

	// review model
	private ReviewModel mReviewModel;
	private CodeReviewListDailog codeReviewListDailog;

	/**
     * Create the dialog.
     * 
     * @param parentShell
     */
	public CloseReviewDialog(Shell parentShell) {
        super(parentShell);
    }

	/**
	 * @param mReviewModel the mReviewModel to set
	 */
	public void setmReviewModel(ReviewModel mReviewModel) {
		this.mReviewModel = mReviewModel;
	}

	/**
	 * @param codeReviewListDailog the codeReviewListDailog to set
	 */
	public void setCodeReviewListDailog(CodeReviewListDailog codeReviewListDailog) {
		this.codeReviewListDailog = codeReviewListDailog;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		// reviewer
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		lblNewLabel.setText("Reviewer");
		Label reviewer = new Label(container, SWT.NONE);
		reviewer.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		reviewer.setText(mReviewModel.getReviewer());

		// coder
		Label coderLbl = new Label(container, SWT.NONE);
		coderLbl.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		coderLbl.setText("Coder");
		Label coder = new Label(container, SWT.NONE);
		coder.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		coder.setText(mReviewModel.getToCoder());

		// comment
		Label commentLbl = new Label(container, SWT.NONE);
		commentLbl.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		commentLbl.setText("Coder");
		StyledText commentTxt = new StyledText(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		commentTxt.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		commentTxt.setText(mReviewModel.getComment());
		// no edit
		commentTxt.setEditable(false);
		GridData comment_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		comment_text.minimumHeight = 100;
		commentTxt.setLayoutData(comment_text);

		// reply
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		lblNewLabel_1.setText("Reply");
		// reply content
		styledText = new StyledText(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.minimumHeight = 100;
		styledText.setLayoutData(gd_styledText);
		if (mReviewModel.getCoderReply() != null) {
			styledText.setText(mReviewModel.getCoderReply());
		}

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		mReviewModel.setCoderReply(styledText.getText());
		mReviewModel.setCompleteTime(new Date());
		mReviewModel.setStatus(StatusType.CLOSE.getType());

		boolean result = new DataServices().updateReviewToClose(mReviewModel);
		if (!result) {
			// tips error
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "错误提示", "数据无法写入数据库！");
		}
		super.okPressed();
		// 刷新数据
		if (mReviewModel.getId() != null) {
			codeReviewListDailog.reviewList(null);
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 700);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Close Review");
		// setting icon
		ImageDescriptor image = CRIcons.codeReviewListDiaIcon;
		newShell.setImage(image.createImage());

		// 设置窗口居中
		Rectangle bounds = Display.getCurrent().getPrimaryMonitor().getBounds();
		Rectangle rect = newShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		newShell.setLocation(x, y);
	}

}

