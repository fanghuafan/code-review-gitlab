/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月17日
 */
package cn.eclipse.code.review.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import cn.eclipse.code.review.ui.dialogs.CodeReviewListDailog;

/**
 * @desciption code review list(list come from mysql database)
 * @author jack_fan
 * @date 2018年12月17日
 */
public class CodeReviewListHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CodeReviewListDailog dialog = new CodeReviewListDailog(Display.getCurrent().getActiveShell());
		dialog.open();
		return null;
	}

}