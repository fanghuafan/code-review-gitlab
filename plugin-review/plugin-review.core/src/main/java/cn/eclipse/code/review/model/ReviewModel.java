/**
 * @desciption
 *
 * @author jack_fan
 * @date 2018年12月18日
 */
package cn.eclipse.code.review.model;

import java.util.Date;

/**
 * @desciption review model
 * @author jack_fan
 * @date 2018年12月18日
 */
public class ReviewModel {
	// key
	private Integer id;
	// reviewer
	private String reviewer;
	// to the coder
	private String toCoder;
	// project name
	private String project;
	// class path
	private String classPath;
	// class absolute path
	private String absoluteClassPath;
	// comment line
	private Integer startLine;
	// comment line
	private Integer endLine;
	// comment time
	private Date commentTime;
	// comment msg
	private String comment;
	// status (0 todo 1 complete)
	private Integer status = 0;
	// 完成时间
	private Date completeTime;
	// current line code
	private String code;
	// is code change (change 1/no change 0)
	private Integer codeChange = 0;
	// comment type (comment line or issue)
	private String commentType;
	// git commitId
	private String commitId;
	// coder reply
	private String coderReply;
	private Integer reviewerId = 0;
	private Integer toCoderId = 0;
	// 自定义review等级
	private String reviewGrade;
	// 删除状态
	private Integer deleteStatus = 0;
	// gitlab关联工程id
	private Integer gitlabProjectId;
	// gitlab 关联评论id（comment or snippets or issue）
	private Integer gitlabCommentId;
	// 仓库名称
	private String repoName;
	// 工程创建人
	private String gitlabOwner;

	/**
	 * @return the gitlabOwner
	 */
	public String getGitlabOwner() {
		return gitlabOwner;
	}

	/**
	 * @param gitlabOwner the gitlabOwner to set
	 */
	public void setGitlabOwner(String gitlabOwner) {
		this.gitlabOwner = gitlabOwner;
	}

	/**
	 * @return the coderReply
	 */
	public String getCoderReply() {
		return coderReply;
	}

	/**
	 * @param coderReply the coderReply to set
	 */
	public void setCoderReply(String coderReply) {
		this.coderReply = coderReply;
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
	 * @return the classPath
	 */
	public String getClassPath() {
		return classPath;
	}

	/**
	 * @param classPath the classPath to set
	 */
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}


	/**
	 * @return the startLine
	 */
	public Integer getStartLine() {
		return startLine;
	}

	/**
	 * @param startLine the startLine to set
	 */
	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}

	/**
	 * @return the endLine
	 */
	public Integer getEndLine() {
		return endLine;
	}

	/**
	 * @param endLine the endLine to set
	 */
	public void setEndLine(Integer endLine) {
		this.endLine = endLine;
	}

	/**
	 * @return the commentTime
	 */
	public Date getCommentTime() {
		return commentTime;
	}

	/**
	 * @param commentTime the commentTime to set
	 */
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the completeTime
	 */
	public Date getCompleteTime() {
		return completeTime;
	}

	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the codeChange
	 */
	public Integer getCodeChange() {
		return codeChange;
	}

	/**
	 * @param codeChange the codeChange to set
	 */
	public void setCodeChange(Integer codeChange) {
		this.codeChange = codeChange;
	}

	/**
	 * @return the commentType
	 */
	public String getCommentType() {
		return commentType;
	}

	/**
	 * @param commentType the commentType to set
	 */
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	/**
	 * @return the commitId
	 */
	public String getCommitId() {
		return commitId;
	}

	/**
	 * @param commitId the commitId to set
	 */
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	/**
	 * @return the reviewerId
	 */
	public Integer getReviewerId() {
		return reviewerId;
	}

	/**
	 * @param reviewerId the reviewerId to set
	 */
	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}

	/**
	 * @return the toCoderId
	 */
	public Integer getToCoderId() {
		return toCoderId;
	}

	/**
	 * @param toCoderId the toCoderId to set
	 */
	public void setToCoderId(Integer toCoderId) {
		this.toCoderId = toCoderId;
	}

	/**
	 * @return the reviewGrade
	 */
	public String getReviewGrade() {
		return reviewGrade;
	}

	/**
	 * @param reviewGrade the reviewGrade to set
	 */
	public void setReviewGrade(String reviewGrade) {
		this.reviewGrade = reviewGrade;
	}

	/**
	 * @return the deleteStatus
	 */
	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	/**
	 * @param deleteStatus the deleteStatus to set
	 */
	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	/**
	 * @return the gitlabProjectId
	 */
	public Integer getGitlabProjectId() {
		return gitlabProjectId;
	}

	/**
	 * @param gitlabProjectId the gitlabProjectId to set
	 */
	public void setGitlabProjectId(Integer gitlabProjectId) {
		this.gitlabProjectId = gitlabProjectId;
	}

	/**
	 * @return the gitlabCommentId
	 */
	public Integer getGitlabCommentId() {
		return gitlabCommentId;
	}

	/**
	 * @param gitlabCommentId the gitlabCommentId to set
	 */
	public void setGitlabCommentId(Integer gitlabCommentId) {
		this.gitlabCommentId = gitlabCommentId;
	}

	/**
	 * @return the absoluteClassPath
	 */
	public String getAbsoluteClassPath() {
		return absoluteClassPath;
	}

	/**
	 * @param absoluteClassPath the absoluteClassPath to set
	 */
	public void setAbsoluteClassPath(String absoluteClassPath) {
		this.absoluteClassPath = absoluteClassPath;
	}

	/**
	 * @return the repoName
	 */
	public String getRepoName() {
		return repoName;
	}

	/**
	 * @param repoName the repoName to set
	 */
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReviewModel [id=" + id + ", reviewer=" + reviewer + ", toCoder=" + toCoder + ", project=" + project
				+ ", classPath=" + classPath + ", absoluteClassPath=" + absoluteClassPath + ", startLine=" + startLine
				+ ", endLine=" + endLine + ", commentTime=" + commentTime + ", comment=" + comment + ", status="
				+ status + ", completeTime=" + completeTime + ", code=" + code + ", codeChange=" + codeChange
				+ ", commentType=" + commentType + ", commitId=" + commitId + ", coderReply=" + coderReply
				+ ", reviewerId=" + reviewerId + ", toCoderId=" + toCoderId + ", reviewGrade=" + reviewGrade
				+ ", deleteStatus=" + deleteStatus + ", gitlabProjectId=" + gitlabProjectId + ", gitlabCommentId="
				+ gitlabCommentId + ", repoName=" + repoName + ", gitlabOwner=" + gitlabOwner + ", getGitlabOwner()="
				+ getGitlabOwner() + ", getCoderReply()=" + getCoderReply() + ", getId()=" + getId()
				+ ", getReviewer()=" + getReviewer() + ", getToCoder()=" + getToCoder() + ", getProject()="
				+ getProject() + ", getClassPath()=" + getClassPath() + ", getStartLine()=" + getStartLine()
				+ ", getEndLine()=" + getEndLine() + ", getCommentTime()=" + getCommentTime() + ", getComment()="
				+ getComment() + ", getStatus()=" + getStatus() + ", getCompleteTime()=" + getCompleteTime()
				+ ", getCode()=" + getCode() + ", getCodeChange()=" + getCodeChange() + ", getCommentType()="
				+ getCommentType() + ", getCommitId()=" + getCommitId() + ", getReviewerId()=" + getReviewerId()
				+ ", getToCoderId()=" + getToCoderId() + ", getReviewGrade()=" + getReviewGrade()
				+ ", getDeleteStatus()=" + getDeleteStatus() + ", getGitlabProjectId()=" + getGitlabProjectId()
				+ ", getGitlabCommentId()=" + getGitlabCommentId() + ", getAbsoluteClassPath()="
				+ getAbsoluteClassPath() + ", getRepoName()=" + getRepoName() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
