package cn.eclipse.code.review.ui.dialogs.viewer;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 创建这个类,主要是对List集合中的数据记录进行筛选和转化. 是"内容器"的作用.
 * 内容器中主要是对setInput()输入的数据集集合(本例子中指的是在PeopleFactory中封装好的List集合)做处理.
 * 并且转换化成一个数组返回.
 * 
 * 实现对应的接口IStructuredContentProvider,然后实现其中的方法.
 * 
 * @author j
 */
public class TableViewerContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	// 实现IStructuredContentProvider接口之后,主要复写的就是这个getElements()方法.
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List<?>) inputElement).toArray();
		} else {
			return new Object[0];
		}
	}
}