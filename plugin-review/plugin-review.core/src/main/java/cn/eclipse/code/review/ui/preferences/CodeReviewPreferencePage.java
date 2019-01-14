package cn.eclipse.code.review.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * code review配置
 * 
 * @author jack_fan
 * @date 2018年12月14日
 */
public class CodeReviewPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	// review描述
	private static final String DESCRIPTION = "对于GitLab功能的描述：\r\n" 
			+ "1、可以添加对应行在GitLab Git对应提交记录行添加comment;\r\n"
			+ "2、同时记录到对应的数据库表，在对应review记录列表中显示，点击列表可以直接跳转到对应行。";
	public CodeReviewPreferencePage() {
		setDescription(DESCRIPTION);
	}

	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));
		Label lbl = new Label(comp, SWT.BOLD);
		lbl.setText("安装插件及配置GitLab和MySQL："); //$NON-NLS-1$

		Text text = new Text(comp, SWT.WRAP | SWT.READ_ONLY);
		text.setText("1、配置GitLab地址、用户名及密码；\r\n2、配置MySQL地址、用户名及密码。\r\n3、配置当前用户信息。");

		return comp;
	}
}
