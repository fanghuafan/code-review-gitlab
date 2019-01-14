/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月22日
 */
package cn.eclipse.code.review.model;

/**
 * @desciption GitLab相关信息
 * @author jack_fan
 * @date 2018年12月22日
 */
public class GitLabConnectionModel {
	// mysql数据库名称
	private String addr;
	// private token
	private String privateToken;
	// is gitlab api v4
	private Boolean isV4;

	public GitLabConnectionModel() {
		super();
	}

	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * @return the privateToken
	 */
	public String getPrivateToken() {
		return privateToken;
	}

	/**
	 * @param privateToken the privateToken to set
	 */
	public void setPrivateToken(String privateToken) {
		this.privateToken = privateToken;
	}

	/**
	 * @return the isV4
	 */
	public Boolean getIsV4() {
		return isV4;
	}

	/**
	 * @param isV4 the isV4 to set
	 */
	public void setIsV4(Boolean isV4) {
		this.isV4 = isV4;
	}

	/**
	 * @param addr
	 * @param privateToken
	 * @param isV4
	 */
	public GitLabConnectionModel(String addr, String privateToken, Boolean isV4) {
		super();
		this.addr = addr;
		this.privateToken = privateToken;
		this.isV4 = isV4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GitLabConnectionModel [addr=" + addr + ", privateToken=" + privateToken + ", isV4=" + isV4
				+ ", getAddr()=" + getAddr() + ", getPrivateToken()=" + getPrivateToken() + ", getIsV4()=" + getIsV4()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}



}
