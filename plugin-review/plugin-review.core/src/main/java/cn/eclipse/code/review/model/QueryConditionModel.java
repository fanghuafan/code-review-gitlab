/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月19日
 */
package cn.eclipse.code.review.model;

import java.util.Date;

/**
 * @desciption 查询条件model
 * @author jack_fan
 * @date 2018年12月19日
 */
public class QueryConditionModel {
	// 代码审查人
	private String reviewer;
	// 代码被审查人
	private String toCoder;
	// 模糊查询关键字
	private String key;
	// 工程名
	private String project;
	// 开始时间
	private Date startTime;
	// 结束时间
	private Date endTime;
	// 翻页页码
	private int page = 1;
	// 每页数量
	private int pageSize = 33;

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the reviewer
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * @param reviewer the reviewer to set
	 */
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * @return the toCoder
	 */
	public String getToCoder() {
		return toCoder;
	}

	/**
	 * @param toCoder the toCoder to set
	 */
	public void setToCoder(String toCoder) {
		this.toCoder = toCoder;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryConditionModel [reviewer=" + reviewer + ", toCoder=" + toCoder + ", key=" + key + ", project="
				+ project + ", startTime=" + startTime + ", endTime=" + endTime + ", page=" + page + ", pageSize="
				+ pageSize + ", getPageSize()=" + getPageSize() + ", getPage()=" + getPage() + ", getReviewer()="
				+ getReviewer() + ", getToCoder()=" + getToCoder() + ", getKey()=" + getKey() + ", getProject()="
				+ getProject() + ", getStartTime()=" + getStartTime() + ", getEndTime()=" + getEndTime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
