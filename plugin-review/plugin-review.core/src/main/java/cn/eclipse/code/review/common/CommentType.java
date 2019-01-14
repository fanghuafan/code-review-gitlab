/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.common;

/**
 * @desciption 评论类型
 * @author jack_fan
 * @date 2018年12月21日
 */
public enum CommentType {
	COMMENT("comment"), ISSUE("issue"), SNIPPETS("snippets");

	// comment type
	private String type;
	private CommentType(String type) {
		this.type = type;
	}

	/**
	 * @param type the type to get
	 */
	public String getType() {
		return type;
	}

	/**
	 * @desciption 获取对应的enum
	 * @author jack_fan
	 * @date 2019年1月14日
	 * @param type
	 * @return
	 */
	public static CommentType getCommentType(String type) {
		if (CommentType.COMMENT.getType().equals(type)) {
			return CommentType.COMMENT;
		} else if (CommentType.ISSUE.getType().equals(type)) {
			return CommentType.ISSUE;
		} else if (CommentType.SNIPPETS.getType().equals(type)) {
			return CommentType.SNIPPETS;
		}
		return null;
	}
}
