package cn.eclipse.code.review.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
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
public class GitLabPerferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String ADDR = CRPlugin.PLUGIN_ID + ".gitlab.addr";
	public static final String PRIVATE_TOKEN = CRPlugin.PLUGIN_ID + ".gitlab.token";
	public static final String VERSION = CRPlugin.PLUGIN_ID + ".gitlab.version";

	/**
	 * Create the preference page.
	 */
	public GitLabPerferencePage() {
		super(GRID);
		setPreferenceStore(CRPlugin.getDefault().getPreferenceStore());
		setDescription(
				"请你输入您的GitLab相关信息，如GitLab地址，private token等信息，以便您能够方便的进行code review。\r\n"
				+ "Thank you! \r\n"
				+ "Best wishes for you!!!");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(ADDR, "Address", getFieldEditorParent()));
		addField(new StringFieldEditor(PRIVATE_TOKEN, "GitLab Token", getFieldEditorParent()));
		addField(new BooleanFieldEditor(VERSION, "GitLab V4", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench paramIWorkbench) {
		// TODO Auto-generated method stub
	}
}
