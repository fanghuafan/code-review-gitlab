package cn.eclipse.code.review.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.eclipse.code.review.CRPlugin;

/**
 * 数据库设置面板
 * 
 * @author jack_fan
 * @date 2018年12月14日
 */
public class DatabasePerferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
	public static final String ADDR = CRPlugin.PLUGIN_ID + ".mysql.addr";
	public static final String USERNAME = CRPlugin.PLUGIN_ID + ".mysql.username";
	public static final String PASSWORD = CRPlugin.PLUGIN_ID + ".mysql.password";

	/**
	 * Create the preference page.
	 */
	public DatabasePerferencePage() {
		super(GRID);
		setPreferenceStore(CRPlugin.getDefault().getPreferenceStore());
		setDescription(
				"请你输入您的MySQL相关信息，如MySQL地址，用户名和密码等信息，以便您能够方便的进行code review。\r\n"
				+ "Thank you! \r\n"
				+ "Best wishes for you!!!");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(ADDR, "地址", getFieldEditorParent()));
		addField(new StringFieldEditor(USERNAME, "用户名", getFieldEditorParent()));
		addField(new StringFieldEditor(PASSWORD, "密码", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench paramIWorkbench) {
		// TODO Auto-generated method stub
	}
    
}
