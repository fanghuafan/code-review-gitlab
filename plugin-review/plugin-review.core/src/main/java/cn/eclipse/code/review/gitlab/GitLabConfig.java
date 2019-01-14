/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月25日
 */
package cn.eclipse.code.review.gitlab;

import java.util.List;

/**
 * @desciption gitlab配置信息
 * @author jack_fan
 * @date 2018年12月25日
 */
public class GitLabConfig {
	// gitlab url
	private String gitlabUrl;
	// private token
	private String privateToken;
	// gitlab is version4
	private boolean isV4;
	// commit log id
	private List<String> commitSHA;

	/**
	 * @return the gitlabUrl
	 */
	public String getGitlabUrl() {
		return gitlabUrl;
	}

	/**
	 * @param gitlabUrl the gitlabUrl to set
	 */
	public void setGitlabUrl(String gitlabUrl) {
		this.gitlabUrl = gitlabUrl;
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
	public boolean isV4() {
		return isV4;
	}

	/**
	 * @param isV4 the isV4 to set
	 */
	public void setV4(boolean isV4) {
		this.isV4 = isV4;
	}

	/**
	 * @return the commitSHA
	 */
	public List<String> getCommitSHA() {
		return commitSHA;
	}

	/**
	 * @param commitSHA the commitSHA to set
	 */
	public void setCommitSHA(List<String> commitSHA) {
		this.commitSHA = commitSHA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GitLabConfig [gitlabUrl=" + gitlabUrl + ", privateToken=" + privateToken + ", isV4=" + isV4
				+ ", commitSHA=" + commitSHA + ", getGitlabUrl()=" + getGitlabUrl() + ", getPrivateToken()="
				+ getPrivateToken() + ", isV4()=" + isV4() + ", getCommitSHA()=" + getCommitSHA() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
