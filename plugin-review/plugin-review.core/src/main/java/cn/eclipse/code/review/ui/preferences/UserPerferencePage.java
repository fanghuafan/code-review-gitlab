package cn.eclipse.code.review.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.eclipse.code.review.CRPlugin;

/**
 * gitlab相关配置
 * 
 * @author jack_fan
 * @date 2018年12月14日
 */
public class UserPerferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String USER_ROOT = CRPlugin.PLUGIN_ID + ".user";
	public static final String ID = CRPlugin.PLUGIN_ID + ".user.id";
	public static final String NAME = CRPlugin.PLUGIN_ID + ".user.name";
	public static final String ENGLISH_NAME = CRPlugin.PLUGIN_ID + ".user.english.name";
	public static final String POSITION = CRPlugin.PLUGIN_ID + ".user.position";

	/**
	 * Create the preference page.
	 */
	public UserPerferencePage() {
		super(GRID);
		setPreferenceStore(CRPlugin.getDefault().getPreferenceStore());
		setDescription(
				"请你输入您的User相关信息，如GitLab地址，用户名和密码等信息，以便您能够方便的进行code review。\r\n"
				+ "Thank you! \r\n"
				+ "Best wishes for you!!!");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(NAME, "中文名", getFieldEditorParent()));
		addField(new StringFieldEditor(ENGLISH_NAME, "英文名", getFieldEditorParent()));
		addField(new StringFieldEditor(POSITION, "职位", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench paramIWorkbench) {
		// TODO Auto-generated method stub
	}
}
