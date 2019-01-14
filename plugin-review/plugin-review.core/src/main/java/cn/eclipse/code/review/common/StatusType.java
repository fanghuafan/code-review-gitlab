/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.common;

/**
 * @desciption review record status
 * @author jack_fan
 * @date 2018年12月21日
 */
public enum StatusType {
	CLOSE(1), OPEN(0);

	private Integer type;

	private StatusType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

}
