/*
 * SonarQube :: GitLab Plugin
 * Copyright (C) 2016-2017 Talanlabs
 * gabriel.allaigre@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cn.eclipse.code.review.gitlab;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.gitlab.api.models.CommitComment;
import org.gitlab.api.models.GitLabSnippets;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNote;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import cn.eclipse.code.review.common.CommentType;

public interface IGitLabApiWrapper {
	/**
	 * @desciption 获取当前用户西悉尼
	 * @author jack_fan
	 * @date 2019年1月8日
	 * @return 当前用户西悉尼
	 */
	public GitlabUser getCurrentUserInfo();

	/**
	 * @desciption 获取当前工程信息
	 * @author jack_fan
	 * @date 2019年1月13日
	 */
	GitlabProject getGitLabProject();

	/**
	 * @desciption 设置配置GitLab文件
	 * @author jack_fan
	 * @date 2019年1月4日
	 * @param config
	 */
	void setConfig(GitLabConfig config);

	/**
	 * @desciption 初始化
	 * @author jack_fan
	 * @date 2018年12月25日
	 */
	void init(String repoName, CommentType type);

	/**
	 * @desciption 创建issue的评论
	 * @author jack_fan
	 * @date 2019年1月14日
	 * @param issue
	 * @param message
	 */
	GitlabNote createIssueNote(GitlabIssue issue, String message);

	/**
	 * @desciption 只初始化API类
	 * @author jack_fan
	 * @date 2019年1月14日
	 * @param repoName
	 */
	void inits(String repoName);

	/**
     * @desciption
     *  Attribute	Type	Required	Description
	 *	title	    String	yes	        The title of a snippet
	 *	file_name	String	yes	        The name of a snippet file
	 *	content	    String	yes	        The content of a snippet
	 *	description	String	no	        The description of a snippet
	 *	visibility	String	no	        The snippet’s visibility
     * @author jack_fan
     * @date 2018年12月25日
     * @return 是否创建代码片段成功
     */
	GitLabSnippets createOrUpdateSippets(Integer id, String title, String fileName, String content, String description,
			String visibility) throws IOException;
	
	/**
	 * @desciption
	 * 
	 *	Attribute	Type	Required	Description
	 *	id	        integer/string	yes	The ID or URL-encoded path of the project owned by the authenticated user
	 *	iid	        integer/string	no	The internal ID of the project’s issue (requires admin or project owner rights)
	 *	title	    string	yes	The title of an issue
	 *	description	string	no	The description of an issue
	 *	confidential	boolean	no	Set an issue to be confidential. Default is false.
	 *	assignee_ids	Array[integer]	no	The ID of the users to assign issue
	 *	milestone_id	integer	no	The global ID of a milestone to assign issue
	 *	labels	string	no	Comma-separated label names for an issue
	 *	created_at	string	no	Date time string, ISO 8601 formatted, e.g. 2016-03-11T03:45:40Z (requires admin or project/group owner rights)
	 *	due_date	string	no	Date time string in the format YEAR-MONTH-DAY, e.g. 2016-03-11
	 *	merge_request_to_resolve_discussions_of	integer	no	The IID of a merge request in which to resolve all issues. This will fill the issue with a default description and mark all discussions as resolved. When passing a description or title, these values will take precedence over the default values.
	 *	discussion_to_resolve	string	no	The ID of a discussion to resolve. This will fill in the issue with a default description and mark the discussion as resolved. Use in combination with merge_request_to_resolve_discussions_of.
	 * @author jack_fan
	 * @date 2018年12月25日
	 * @param issueId
	 * @param assigneeId
	 * @param milestoneId
	 * @param labels
	 * @param description
	 * @param title
	 * @param action
	 * @return 是否操作成功
	 */
	GitlabIssue createOrUpdateIssue(
			Integer issueId, 
			Integer assigneeId, 
			Integer milestoneId,
			String labels,
			String description, 
			String title, 
			GitlabIssue.Action action) throws IOException;

	/**
	 * @desciption 获取当前行提交的commit id
	 * @author jack_fan
	 * @date 2018年12月25日
	 * @param file
	 * @param path
	 * @param lineNumber
	 * @return
	 */
	String getRevisionForLine(File file, String path, int lineNumber);

	/**
	 * @desciption 创建或者更新代码评论
	 * @author jack_fan
	 * @date 2018年12月25日
	 * @param revision
	 * @param fullPath
	 * @param line
	 * @param body
	 */
	CommitComment createReviewComment(String revision, String fullPath, Integer line, String body);

	/**
	 * @desciption 行信息
	 * @author jack_fan
	 * @date 2018年12月25日
	 */
    class Line {
        private Integer number;
        private String content;

        Line(Integer number, String content) {
            this.number = number;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Line{" + "number=" + number +
                    ", content='" + content + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Line line = (Line) o;
            return Objects.equals(number, line.number) &&
                    Objects.equals(content, line.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(number, content);
        }
    }
}
