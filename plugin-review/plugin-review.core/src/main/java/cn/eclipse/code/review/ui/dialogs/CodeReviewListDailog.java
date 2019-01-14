/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月17日
 */
package cn.eclipse.code.review.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.eclipse.code.review.common.Constants;
import cn.eclipse.code.review.model.QueryConditionModel;
import cn.eclipse.code.review.model.UserModel;
import cn.eclipse.code.review.services.DataServices;
import cn.eclipse.code.review.ui.dialogs.viewer.CodeReviewviewTableViewer;
import cn.eclipse.code.review.ui.icon.CRIcons;

/**
 * @desciption get the code result, view the comment and issue
 * @author jack_fan
 * @date 2018年12月17日
 */
public class CodeReviewListDailog extends Dialog {
	private Composite container = null;
	private Composite reviewContainer = null;
	private CodeReviewviewTableViewer view = null;
	private Button currentButton = null;
	/**
     * Create the dialog.
     * 
     * @param parentShell
     */
	public CodeReviewListDailog(Shell parentShell) {
        super(parentShell);
    }

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		// search condition
		queryView(container);

		// review list， 开始默认查询全部
		reviewList(null);

		return container;
	}

	/**
	 * @desciption 设置review list控件
	 * @author jack_fan
	 * @date 2018年12月23日
	 */
	public void reviewList(QueryConditionModel condition) {
		if (reviewContainer != null) {
			// 释放，然后再创建新的控件
			reviewContainer.dispose();
		}
		reviewContainer = new Composite(container, SWT.FILL);
		reviewContainer.setLayout(new FillLayout(SWT.VERTICAL));
		reviewContainer.setLayoutData(new GridData(1270, 825));
		view = new CodeReviewviewTableViewer();
		view.setCodeReviewListDailog(this);
		view.init(reviewContainer, condition == null ? new QueryConditionModel() : condition);

		// 重画
		container.layout();
	}

	/**
	 * @desciption 添加搜索过滤条件
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param container
	 */
	private void queryView(Composite container) {
		Composite searchContainer = new Composite(container, SWT.NONE);
		searchContainer.setLayout(new GridLayout(9, false));

		// 工程信息
		// 定义一个只读的下拉框
		final Combo combo = new Combo(searchContainer, SWT.ALL);
		combo.add("---please select---", 0);
		combo.setBounds(16, 11, 270, 25);
		combo.setLayoutData(new GridData(100, -1));
		DataServices service = new DataServices();
		List<String> projects = service.getPrjectList();
		if (projects != null) {
			int index = 1;
			for (String item : projects) {
				combo.add(item == null ? "(void)" : item, index++);
			}
			if (projects.size() > 0) {
				combo.select(0);
			}
		}

		// coder信息
		// 定义一个只读的下拉框
		final Combo coderCombo = new Combo(searchContainer, SWT.ALL);
		coderCombo.add("---please select---", 0);
		coderCombo.setBounds(16, 11, 270, 25);
		coderCombo.setLayoutData(new GridData(200, -1));
		// 设置数据
		List<UserModel> users = service.getAllUsers();
		if (users != null) {
			int index = 1;
			for (UserModel item : users) {
				coderCombo.add(item.getEnglishName() + "/" + item.getName() + "-" + item.getPosition(), index++);
			}
			if (users.size() > 0) {
				coderCombo.select(0);
			}
		}

		// 时间过滤
		CDateTime start_dt = new CDateTime(searchContainer,
				CDT.BORDER | CDT.DATE_SHORT | CDT.TIME_SHORT | CDT.DROP_DOWN);// or SWT.TIME
		start_dt.setPattern("yyyy-MM-dd HH:mm:ss");
		Label lbl = new Label(searchContainer, SWT.NONE);
		lbl.setText("-");
		CDateTime end_dt = new CDateTime(searchContainer, CDT.BORDER | CDT.DATE_SHORT | CDT.TIME_SHORT | CDT.DROP_DOWN); // or
																															// SWT.TIME
		end_dt.setPattern("yyyy-MM-dd HH:mm:ss");
		end_dt.setLayoutData(new GridData(130, -1));

		// 模糊查询
		Text text = new Text(searchContainer, SWT.SEARCH);
		String searchTip = "Please enter your search key……";
		text.setToolTipText(searchTip);
		text.setLayoutData(new GridData(230, -1));
		text.setText(searchTip);
		text.addListener(SWT.FocusIn, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (searchTip.equals(text.getText())) {
					text.setText("");
				}
			}
		});
		text.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event e) {
				if ("".equals(text.getText())) {
					text.setText(searchTip);
				}
			}
		});

		// 是否显示个人review list
		Button myReview_btn = new Button(searchContainer, SWT.CENTER | SWT.CHECK);
		myReview_btn.setText("My Review");
		myReview_btn.setLayoutData(new GridData(100, -1));

		// 查询按钮
		Button search = new Button(searchContainer, SWT.CENTER);
		search.setText("search");
		search.setLayoutData(new GridData(80, -1));
		search.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 刷新table view数据，获取过滤的搜索结果
				QueryConditionModel condition = new QueryConditionModel();
				// 工程名称
				String selectProject = combo.getSelectionIndex() == 0 ? null
						: projects.get(combo.getSelectionIndex() - 1);
				condition.setProject(selectProject);
				// coder过滤值
				UserModel coder = coderCombo.getSelectionIndex() == 0 ? null
						: users.get(coderCombo.getSelectionIndex() - 1);
				if (coder != null) {
					condition.setToCoder(coder.getName());
				}
				// 时间过滤
				condition.setStartTime(start_dt.getSelection());
				condition.setEndTime(end_dt.getSelection());
				// 获取模糊搜索值
				condition.setKey(searchTip.equals(text.getText()) ? null : text.getText());

				// dispose旧的控件，创建新的控件
				reviewList(condition);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		// 清除初始化查询
		Button clear_btn = new Button(searchContainer, SWT.CENTER);
		clear_btn.setText("clear");
		clear_btn.setLayoutData(new GridData(80, -1));
		clear_btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// dispose旧的控件，创建新的控件
				reviewList(null);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);

		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		currentButton = new Button(parent, SWT.PUSH);
		currentButton.setText("当前第" + CodeReviewviewTableViewer.page + "页");
		currentButton.setFont(JFaceResources.getDialogFont());
		this.setButtonLayoutData(currentButton);

		((GridLayout) parent.getLayout()).numColumns++;
		Button preButton = new Button(parent, SWT.PUSH);
		preButton.setText("上一页");
		preButton.setFont(JFaceResources.getDialogFont());
		preButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				view.prePage();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		this.setButtonLayoutData(preButton);

		((GridLayout) parent.getLayout()).numColumns++;
		Button nextButton = new Button(parent, SWT.PUSH);
		nextButton.setText("下一页");
		nextButton.setFont(JFaceResources.getDialogFont());
		nextButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				view.nextPage();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		this.setButtonLayoutData(nextButton);

		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button buttonTotal = new Button(parent, SWT.PUSH);
		buttonTotal.setText("共" + CodeReviewviewTableViewer.totalPage + "页");
		buttonTotal.setFont(JFaceResources.getDialogFont());
		this.setButtonLayoutData(buttonTotal);
	}

	/**
	 * @desciption 设置当前页码
	 * @author jack_fan
	 * @date 2019年1月13日
	 * @param page
	 */
	public void setCurrentPageNumber(int page) {
		if (currentButton != null) {
			currentButton.setText("当前第" + page + "页");
		}
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
		newShell.setSize(1270, 950);
		newShell.setLayout(new FillLayout());
		newShell.setText(Constants.CODE_REVIEW_LIST);
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
