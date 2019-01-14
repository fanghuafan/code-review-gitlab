/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月24日
 */
package cn.eclipse.code.review.common;

import java.util.Date;

/**
 * @desciption 时间格式化
 * @author jack_fan
 * @date 2018年12月24日
 */
public class DateUtils {
	/**
	 * @desciption 时间格式化
	 * @author jack_fan
	 * @date 2018年12月24日
	 * @param date
	 * @return 格式化后的时间
	 */
	public static String dateTimeFormat(Date date) {
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format1.format(date);
	}
}
