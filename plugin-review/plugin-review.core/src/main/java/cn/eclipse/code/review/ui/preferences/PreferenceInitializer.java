package cn.eclipse.code.review.ui.preferences;

import java.util.Date;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.StoreType;
import cn.eclipse.code.review.database.MySQLHelper;
import cn.eclipse.code.review.model.UserModel;
import cn.eclipse.code.review.services.DataServices;
import cn.eclipse.code.review.ui.preferences.store.Store;

/**
 * @desciption 首选项安装信息
 * @author jack_fan
 * @date 2018年12月22日
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	/**
	 * 安装初始化首选项信息
	 */
	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = CRPlugin.getDefault().getPreferenceStore();

		// 添加value改变监听
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getNewValue() instanceof String) {
					store.setDefault(event.getProperty(), (String) event.getNewValue());
				} else if (event.getNewValue() instanceof Boolean) {
					store.setDefault(event.getProperty(), (Boolean) event.getNewValue());
				}
				// 判断是否是User表存储变化
				if (event.getProperty() != null && event.getProperty().contains(UserPerferencePage.USER_ROOT)) {
					UserModel info = (UserModel) Store.getStoreInfo(StoreType.USER);
					if (info != null) {
						info = updateNewValue(info, event.getProperty(), (String) event.getNewValue());
						DataServices services = new DataServices();
						// 需更新
						if (info.getId() == null || info.getId() == 0) {
							info.setCreateTime(new Date());
							Integer userId = services.addUser(info);
							if (userId != null) {
								store.setValue(UserPerferencePage.ID, userId.toString());
							}
						} else {// 需添加
							// 更新User信息
							services.updateUser(info);
						}
					}
				}
				// 判断是否是更新数据库信息
				if (event.getProperty() != null && event.getProperty().contains(DatabasePerferencePage.ROOT)) {
					// 关闭旧的链接，打开新的连接
					MySQLHelper.close();
				}
			}
		});
		// 设置默认值
		setDefault(store);
	}

	/**
	 * @desciption 设置新值
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param user     用户model
	 * @param property 属性值
	 * @param newValue 设置的新值
	 * @return user model
	 */
	private UserModel updateNewValue(UserModel user, String property, String newValue) {
		switch (property) {
		case UserPerferencePage.NAME:
			user.setName(newValue);
			break;
		case UserPerferencePage.ENGLISH_NAME:
			user.setEnglishName(newValue);
			break;
		case UserPerferencePage.POSITION:
			user.setPosition(newValue);
			break;
		}
		return user;
	}

	/**
	 * @desciption 设置默认值
	 * @author jack_fan
	 * @date 2018年12月22日
	 * @param store
	 */
	private void setDefault(IPreferenceStore store) {
		// GitLab配置相关
		store.setDefault(GitLabPerferencePage.ADDR, "");
		store.setDefault(GitLabPerferencePage.PRIVATE_TOKEN, "");
		store.setDefault(GitLabPerferencePage.VERSION, true);
		// MySQL配置相关
		store.setDefault(DatabasePerferencePage.ADDR, "");
		store.setDefault(DatabasePerferencePage.USERNAME, "");
		store.setDefault(DatabasePerferencePage.PASSWORD, "");
		// USer配置相关
		store.setDefault(UserPerferencePage.ID, "");
		store.setDefault(UserPerferencePage.NAME, "");
		store.setDefault(UserPerferencePage.ENGLISH_NAME, "");
		store.setDefault(UserPerferencePage.POSITION, "");
		// HotKey配置相关
		store.setDefault(HotKeyPreferencePage.KEY_ADD, "");
		store.setDefault(HotKeyPreferencePage.KEY_VIEW, "");
	}

}