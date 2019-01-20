/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Shell;

import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.ui.icon.CRIcons;

/**
 * @desciption 查看reviewer comment
 * @author jack_fan
 * @date 2018年12月21日
 */
public class ViewReviewDialog extends Dialog {
	// review model
	private ReviewModel mReviewModel;

	/**
     * Create the dialog.
     * 
     * @param parentShell
     */
	public ViewReviewDialog(Shell parentShell) {
        super(parentShell);
    }

	/**
	 * @param mReviewModel the mReviewModel to set
	 */
	public void setmReviewModel(ReviewModel mReviewModel) {
		this.mReviewModel = mReviewModel;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		// title content
		StyledText titleTxt = new StyledText(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		titleTxt.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		titleTxt.setText(mReviewModel.getTitle());
		// no edit
		titleTxt.setEditable(false);
		GridData title_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		title_text.minimumHeight = 30;
		title_text.grabExcessVerticalSpace = true;
		titleTxt.setLayoutData(title_text);

		// comment content
		StyledText commentTxt = new StyledText(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		commentTxt.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		commentTxt.setText(mReviewModel.getComment());
		// no edit
		commentTxt.setEditable(false);
		GridData comment_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		comment_text.minimumHeight = 500;
		comment_text.grabExcessVerticalSpace = true;
		commentTxt.setLayoutData(comment_text);

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
	}

	@Override
	protected void okPressed() {
		super.okPressed();
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
		newShell.setText("View Review");

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