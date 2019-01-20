/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.eclipse.code.review.ui.dialogs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.eclipse.code.review.common.CommentType;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.model.UserModel;
import cn.eclipse.code.review.repository.CodeReviewRepositoryProvider;
import cn.eclipse.code.review.services.DataServices;
import cn.eclipse.code.review.ui.icon.CRIcons;
import cn.eclipse.code.review.ui.preferences.store.Store;

/**
 * @desciption code review dialogs, add the comment and issue, to save the mysql
 *             and write to gitlab api
 * @author jack_fan
 * @date 2018年12月16日
 */
public class ReviewDialog extends Dialog {
	private static final String CODE_VIEW_HEADER = "///////////////////////////////////code////////////////////////////////\r\n";
	private static final String COMMENT_VIEW_HEADER = "////////////////////////////////comment/////////////////////////////\r\n";
    private Text text;
    StyledText styledText;
	StyledText titleText;
    IFile file;
    TextSelection ts;
	// 单选按钮
	private String selectRadio = "comment";
	// reviewer对应的map
	private Map<Integer, UserModel> reviewerMap = new HashMap<>();
	// reviewer选择的下标
	private Integer chooseIndex = 0;

	// review model
	private ReviewModel mReviewModel;
	// code review dialogs
	private CodeReviewListDailog codeReviewListDailog;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ReviewDialog(Shell parentShell) {
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
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Location");
        
		// comment class and line
        text = new Text(container, SWT.WRAP | SWT.MULTI);
        text.setEditable(false);
		text.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                
		// comment title
		Label title_lbl = new Label(container, SWT.NONE);
		title_lbl.setText("Title");

		titleText = new StyledText(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_titleText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_titleText.minimumHeight = 30;
		titleText.setLayoutData(gd_titleText);

		// comment content
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setText("Content");
        
        styledText = new StyledText(container,
                SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.minimumHeight = 250;
        styledText.setLayoutData(gd_styledText);
                
		if (mReviewModel == null) {
			initData();
		}
        
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Add to");

		// 单选按钮选中
		SelectionAdapter selectAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.getSource();
				selectRadio = b.getText();
			}
		};

		Composite container_send_type = new Composite(container, SWT.NONE);
		container_send_type.setLayout(new GridLayout(3, false));
		container_send_type.setVisible(true);
		// comment radio
		Button comment_radio = new Button(container_send_type, SWT.RADIO | SWT.LEFT);
		comment_radio.setText("comment");
		comment_radio.setSelection(true);
		comment_radio.addSelectionListener(selectAdapter);
		// issue radio
		Button issue_radio = new Button(container_send_type, SWT.RADIO | SWT.LEFT);
		issue_radio.setText("issue");
		issue_radio.addSelectionListener(selectAdapter);
		// snippets radio
		Button snippets_radio = new Button(container_send_type, SWT.RADIO | SWT.LEFT);
		snippets_radio.setText("snippets");
		snippets_radio.addSelectionListener(selectAdapter);

		// tips
		Label tip_lbl_null = new Label(container, SWT.NONE);
		tip_lbl_null.setText("to");

		final Combo combo = new Combo(container, SWT.ALL); // 定义一个只读的下拉框
		// 选择改变监听
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chooseIndex = combo.getSelectionIndex();
			}
		});
		combo.setBounds(16, 11, 270, 25);
		combo.setSize(300, -1);
		// 设置数据
		DataServices service = new DataServices();
		List<UserModel> users = service.getAllUsers();
		if (users != null) {
			int index = 0;
			for (UserModel item : users) {
				reviewerMap.put(index, item);
				combo.add(item.getEnglishName() + "/" + item.getName() + "-" + item.getPosition(), index++);
			}
			if (users.size() > 0) {
				combo.select(0);
			}
		}

		// update init
		updateInit(comment_radio, issue_radio, snippets_radio, combo);

        return container;
    }

	/**
	 * @desciption 更新的时候初始化数据结构
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	private void updateInit(Button comment_radio, Button issue_radio, Button snippets_radio, Combo combo) {
		if (mReviewModel == null) {
			return;
		}
		int line = mReviewModel.getStartLine() == null ? 0 : mReviewModel.getStartLine();
		line++;
		String text = mReviewModel.getComment() == null ? "" : mReviewModel.getComment();
		String tText = mReviewModel.getTitle() == null ? "" : mReviewModel.getTitle();
		String msg = String.format("Code: %s:%s ", mReviewModel.getClassPath(), line);
		// 设置文件路径以及行位置
		this.text.setText(msg);
		// 设置comment content
		styledText.setText(text);
		titleText.setText(tText);

		// 设置content_type
		selectRadio = mReviewModel.getCommentType();
		if (CommentType.COMMENT.getType().equalsIgnoreCase(selectRadio)) {
			comment_radio.setSelection(true);
			issue_radio.setSelection(false);
			snippets_radio.setSelection(false);
		} else if (CommentType.ISSUE.getType().equalsIgnoreCase(selectRadio)) {
			issue_radio.setSelection(true);
			comment_radio.setSelection(false);
			snippets_radio.setSelection(false);
		} else if (CommentType.SNIPPETS.getType().equalsIgnoreCase(selectRadio)) {
			snippets_radio.setSelection(true);
			comment_radio.setSelection(false);
			issue_radio.setSelection(false);
		}
		// 选择to_coder人
		// 遍历MAP获取coder下标
		for (Map.Entry<Integer, UserModel> entry : reviewerMap.entrySet()) {
			UserModel user = entry.getValue();
			if (user != null && user.getId() != null 
					&& mReviewModel.getToCoderId()!= null 
					&& user.getId().equals(mReviewModel.getToCoderId())) {
				chooseIndex = entry.getKey();
				combo.select(chooseIndex);
				break;
			}
		}
	}

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
                
//        getButton(IDialogConstants.OK_ID).setEnabled();
    }
    
    @Override
	protected void okPressed() {
		// 获取当前检点文件绝对路径
		String path = null;
		if (file != null) {
			path = ((IFile) file).getLocation().makeAbsolute().toFile().getAbsolutePath();
		} else {
			if(mReviewModel!=null) {
				path = mReviewModel.getClassPath();
			}
		}
		if (mReviewModel == null) {
			mReviewModel = new ReviewModel();
			if (file != null) {
				mReviewModel.setProjectClassPath(file.getFullPath().toOSString());
			}
		}
		UserModel toCoder = reviewerMap.get(chooseIndex);
		if (toCoder == null) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "错误提示", "没有找到对应的coder！");
			return;
		}
		UserModel reviewer = (UserModel) Store.getStoreInfo(StoreType.USER);
		if (reviewer == null) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "错误提示",
					"没有找到对应的reviewer，请确认是否已经配置User！");
			return;
		}
		if (path != null) {
			path = path.replace("\\", "/");
			String repoName = CodeReviewRepositoryProvider.findGitRepositoryByProjectFilePath(path);
			mReviewModel.setRepoName(repoName);
		}
		mReviewModel.setReviewer(reviewer.getName());
		mReviewModel.setReviewerId(reviewer.getId());
		mReviewModel.setComment(styledText.getText());
		mReviewModel.setTitle(titleText.getText());
		mReviewModel.setToCoder(toCoder.getName());
		mReviewModel.setToCoderId(toCoder.getId());
		mReviewModel.setCommentTime(new Date());
		if (ts != null) {
			mReviewModel.setCode(ts.getText());
			mReviewModel.setStartLine(ts.getStartLine());
			mReviewModel.setEndLine(ts.getEndLine());
		}
		if (file != null) {
			mReviewModel.setProject(getProjectName(file.getFullPath().toString()));
			mReviewModel.setClassPath(path);
		}
		// 默认是comment
		mReviewModel.setCommentType(selectRadio);
		mReviewModel.setAbsoluteClassPath(path);

		// 提示保存
		boolean isOk = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "保存提示",
				"您是否确定保存Review Info到数据库？");
		if (isOk) {
			boolean result = new DataServices().handleReviewToDB(mReviewModel);
			if (!result) {
				// tips error
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "错误提示",
						"数据无法写入数据库！（原因可能是写入gitlab失败）");
				return;
			}

			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "成功提示", "您成功保存review info!");

			super.okPressed();
			// 刷新数据
			if (codeReviewListDailog != null && mReviewModel.getId() != null) {
				codeReviewListDailog.reviewList(null);
			}
		}
    }
    
	/**
	 * @desciption 从文件路径中解析出工程名
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param filePath 文件路径
	 * @return 工程名
	 */
	private String getProjectName(String filePath) {
		if (filePath == null) {
			return null;
		}
		String[] dirList = filePath.split("/");
		return dirList[1];
	}

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
		return new Point(550, 570);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Code Review");

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
    
    public void setData(IFile file, TextSelection ts) {
        this.file = file;
        this.ts = ts;
    }
    
    private void initData() {
        if (file != null) {
            int line = ts == null ? 0 : ts.getStartLine();
            line++;
            String text = ts == null ? "" : ts.getText();
            String msg = String.format("Code: %s:%s ", file.getFullPath(),
                    line);
            this.text.setText(msg);
			styledText.setText((text.trim().length() > 0 ? CODE_VIEW_HEADER : "") + text
					+ (text.trim().length() > 0 ? "\r\n" : "") + COMMENT_VIEW_HEADER);
        }
    }
}
