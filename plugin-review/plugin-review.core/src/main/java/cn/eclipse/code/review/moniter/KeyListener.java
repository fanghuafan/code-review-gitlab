/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月27日
 */
package cn.eclipse.code.review.moniter;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.handlers.ReviewHandler;
import cn.eclipse.code.review.ui.dialogs.CodeReviewListDailog;
import cn.eclipse.code.review.ui.preferences.HotKeyFieldEditor;
import cn.eclipse.code.review.ui.preferences.HotKeyPreferencePage;

/**
 * @desciption 快捷键监听
 * @author jack_fan
 * @date 2018年12月27日
 */
public class KeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent e) {
		String key = HotKeyFieldEditor.keyEvent2String(e);
		IPreferenceStore store = CRPlugin.getDefault().getPreferenceStore();
		// 增加review
		if (key.equals(store.getString(HotKeyPreferencePage.KEY_ADD))) {
			e.doit = false;
			// open add review dialogs
			ReviewHandler.openReviewDialogs();
		}
		// 查看review列表
		else if (key.equals(store.getString(HotKeyPreferencePage.KEY_VIEW))) {
			e.doit = false;
			// open review list dialogs
			CodeReviewListDailog dialog = new CodeReviewListDailog(Display.getCurrent().getActiveShell());
			dialog.open();
		}
	}
}
