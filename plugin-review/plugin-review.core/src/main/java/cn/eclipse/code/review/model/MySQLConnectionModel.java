/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.model;

/**
 * @desciption mysql数据库连接model
 * @author jack_fan
 * @date 2018年12月18日
 */
public class MySQLConnectionModel {
	// mysql数据库名称
	private String addr;
	// driver名称
	private String dirver = "com.mysql.cj.jdbc.Driver";
	// 用户名
	private String username;
	// 密码
	private String password;

	/**
	 * @param addr
	 * @param username
	 * @param password
	 */
	public MySQLConnectionModel(String addr, String username, String password) {
		super();
		this.addr = addr;
		this.username = username;
		this.password = password;
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
	 * @return the dirver
	 */
	public String getDirver() {
		return dirver;
	}

	/**
	 * @param dirver the dirver to set
	 */
	public void setDirver(String dirver) {
		this.dirver = dirver;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MySQLConnectionModel [addr=" + addr + ", dirver=" + dirver + ", username=" + username + ", password="
				+ password + ", getAddr()=" + getAddr() + ", getDirver()=" + getDirver() + ", getUsername()="
				+ getUsername() + ", getPassword()=" + getPassword() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
}
