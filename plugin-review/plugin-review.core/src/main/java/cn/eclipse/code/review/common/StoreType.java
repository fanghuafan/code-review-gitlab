/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.common;

/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
public enum StoreType {
	GITLAB("gitlab"), MYSQL("mysql"), USER("user");

	private String type;

	private StoreType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}
