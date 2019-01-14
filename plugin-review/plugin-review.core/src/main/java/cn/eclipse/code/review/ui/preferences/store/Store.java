/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.ui.preferences.store;

import org.eclipse.jface.preference.IPreferenceStore;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.model.GitLabConnectionModel;
import cn.eclipse.code.review.model.MySQLConnectionModel;
import cn.eclipse.code.review.model.UserModel;
import cn.eclipse.code.review.ui.preferences.DatabasePerferencePage;
import cn.eclipse.code.review.ui.preferences.GitLabPerferencePage;
import cn.eclipse.code.review.ui.preferences.UserPerferencePage;

/**
 * @desciption database connection info
 * @author jack_fan
 * @date 2018年12月18日
 */
public class Store {
	/**
	 * @desciption get the database or gitlab connection info
	 * @author jack_fan
	 * @date 2018年12月18日
	 * @return connection info model
	 */
	public static Object getStoreInfo(StoreType type) {
		IPreferenceStore store = CRPlugin.getDefault().getPreferenceStore();
		switch (type) {
		case GITLAB:
			return new GitLabConnectionModel(store.getString(GitLabPerferencePage.ADDR),
					store.getString(GitLabPerferencePage.PRIVATE_TOKEN),
					store.getBoolean(GitLabPerferencePage.VERSION));
		case MYSQL:
			return new MySQLConnectionModel(store.getString(DatabasePerferencePage.ADDR),
					store.getString(DatabasePerferencePage.USERNAME), store.getString(DatabasePerferencePage.PASSWORD));
		case USER:
			return new UserModel(store.getInt(UserPerferencePage.ID), store.getString(UserPerferencePage.NAME),
					store.getString(UserPerferencePage.ENGLISH_NAME), store.getString(UserPerferencePage.POSITION));
		}
		return null;
	}
}
