/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.ui.icon;

import org.eclipse.jface.resource.ImageDescriptor;

import cn.eclipse.code.review.CRPlugin;

/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
public class CRIcons {
	public static abstract class IconLoader {
		public static ImageDescriptor getIcon(String path) {
			return CRPlugin.getImageDescriptor(path);
		}

		public static ImageDescriptor getShare(String name) {
			return CRPlugin.getSharedImage(name);
		}
	}

	// code review图片
	public static ImageDescriptor codeReviewListDiaIcon = IconLoader.getIcon("/icons/review.png");
	public static ImageDescriptor addIcon = IconLoader.getIcon("/icons/add.png");
	public static ImageDescriptor openIcon = IconLoader.getIcon("/icons/open.png");
	public static ImageDescriptor editIcon = IconLoader.getIcon("/icons/edit.png");
	public static ImageDescriptor skipIcon = IconLoader.getIcon("/icons/gitlab.png");
	public static ImageDescriptor refreshIcon = IconLoader.getIcon("/icons/refresh.png");
	public static ImageDescriptor closeIcon = IconLoader.getIcon("/icons/close.png");
}
