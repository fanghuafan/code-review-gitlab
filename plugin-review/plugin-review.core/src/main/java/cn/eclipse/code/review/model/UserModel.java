/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月21日
 */
package cn.eclipse.code.review.model;

import java.util.Date;

/**
 * @desciption 用户模型
 * @author jack_fan
 * @date 2018年12月21日
 */
public class UserModel {
	private Integer id;
	private String name;
	private String englishName;
	private String position;
	private Integer gitlabRelativeId = 0;
	private Date createTime;

	/**
	 * 
	 */
	public UserModel() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param englishName
	 * @param position
	 */
	public UserModel(Integer id, String name, String englishName, String position) {
		super();
		this.id = id;
		this.name = name;
		this.englishName = englishName;
		this.position = position;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}

	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the gitlabRelativeId
	 */
	public Integer getGitlabRelativeId() {
		return gitlabRelativeId;
	}

	/**
	 * @param gitlabRelativeId the gitlabRelativeId to set
	 */
	public void setGitlabRelativeId(Integer gitlabRelativeId) {
		this.gitlabRelativeId = gitlabRelativeId;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserModel [id=" + id + ", name=" + name + ", englishName=" + englishName + ", position=" + position
				+ ", gitlabRelativeId=" + gitlabRelativeId + ", createTime=" + createTime + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getEnglishName()=" + getEnglishName() + ", getPosition()="
				+ getPosition() + ", getGitlabRelativeId()=" + getGitlabRelativeId() + ", getCreateTime()="
				+ getCreateTime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
