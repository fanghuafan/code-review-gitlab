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
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.CommitComment;
import org.gitlab.api.models.GitLabSnippets;
import org.gitlab.api.models.GitlabCommitDiff;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabIssue.Action;
import org.gitlab.api.models.GitlabNote;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import cn.eclipse.code.review.CRPlugin;
import cn.eclipse.code.review.common.CommentType;

/**
 * @desciption GitLab V4 API
 * @author jack_fan
 * @date 2018年12月25日
 */
public class GitLabApiV4Wrapper implements IGitLabApiWrapper {
	private CRPlugin log = new CRPlugin();
	private GitlabAPI gitLabAPIV4;
	private GitlabProject gitLabProject;
	private GitLabConfig config;
	private Map<String, Map<String, Set<Line>>> patchPositionByFile;

	/**
	 * @param config the config to set
	 */
	@Override
	public void setConfig(GitLabConfig config) {
		this.config = config;
	}

	@Override
	public void init(String repoName, CommentType type) {
		gitLabAPIV4 = GitlabAPI.connect(config.getGitlabUrl(), config.getPrivateToken());
		// 获取当前工程在GitLab上的信息
		gitLabProject = getCurrentProjectInfo(repoName);
		log.log("gitlab project info:" + gitLabProject);
		if (type == CommentType.COMMENT) {
			setCommitInfo();
		}
	}

	/**
	 * @desciption 只有针对comment commit才操作
	 * @author jack_fan
	 * @date 2019年1月13日
	 */
	private void setCommitInfo() {
		try {
			patchPositionByFile = getPatchPositionsToLineMapping(config.getCommitSHA());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @desciption 获取工程信息
	 * @author jack_fan
	 * @date 2019年1月13日
	 * @return 工程信息
	 */
	@Override
	public GitlabProject getGitLabProject() {
		return gitLabProject;
	}

	@Override
	public void inits(String repoName) {
		gitLabAPIV4 = GitlabAPI.connect(config.getGitlabUrl(), config.getPrivateToken());
	}

	/**
	 * @desciption 获取当前工程信息
	 * @author jack_fan
	 * @date 2019年1月5日
	 * @param repoName
	 * @return 当前工程GitLab信息
	 */
	public GitlabProject getCurrentProjectInfo(String repoName) {
		List<GitlabProject> projects = gitLabAPIV4.getAllProjects();
		if (projects != null) {
			for (GitlabProject project : projects) {
				// 判断是否是当前工程
				if (project.getName() != null && project.getName().equals(repoName)) {
					return project;
				}
			}
		}
		return null;
	}

    @Override
    public String getRevisionForLine(File file, String path, int lineNumber) {
        String value = null;
        try {
            List<String> ss = Files.readAllLines(file.toPath());
			int l = lineNumber > 0 ? lineNumber : 0;
			value = ss.size() >= lineNumber ? ss.get(l - 1) : null;
        } catch (IOException e) {
			System.out.println("Not read all line for file {}" + file + e);
        }
        Line line = new Line(lineNumber, value);

		for (String revision : config.getCommitSHA()) {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v ->
                    v.getKey().equals(path) && v.getValue().contains(line))) {
                return revision;
            }
        }
        return null;
    }

	/**
	 * 创建commit comment
	 */
    @Override
	public CommitComment createReviewComment(String revision, String fullPath, Integer line, String body) {
        try {
			return gitLabAPIV4.createCommitComment(gitLabProject.getId(),
					revision != null ? revision : getFirstCommitSHA(),
					body, fullPath, line == null ? null : line.toString(), "new");
        } catch (IOException e) {
			throw new IllegalStateException("Unable to create review comment in file " + fullPath + " at line " + line,
					e);
        }
    }

	/**
	 * @desciption 获取每一次文件commit行信息
	 * @author jack_fan
	 * @date 2018年12月25日
	 * @param revisions
	 * @return d9e34ee12054f8b78adbe8599baf64fd3143bec3
	 * @throws IOException
	 */
	private Map<String, Map<String, Set<Line>>> getPatchPositionsToLineMapping(List<String> revisions)
			throws IOException {
		if (gitLabProject == null) {
			return null;
		}
		Map<String, Map<String, Set<Line>>> result = new HashMap<>();
		for (String revision : revisions) {
			List<GitlabCommitDiff> commitDiffs = gitLabAPIV4.getCommitDiffs(gitLabProject.getId(), revision);

			result.put(revision, commitDiffs.stream().collect(Collectors.toMap(GitlabCommitDiff::getNewPath,
					d -> PatchUtils.getPositionsFromPatch(d.getDiff()))));
		}
		return result;
	}

	/**
	 * @desciption 获取首次提交的commit log
	 * @author jack_fan
	 * @date 2018年12月25日
	 * @return first commit log
	 */
	private String getFirstCommitSHA() {
		return config.getCommitSHA() != null && !config.getCommitSHA().isEmpty() ? config.getCommitSHA().get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.eclipse.code.review.gitlab.IGitLabApiWrapper#createOrUpdateSippets(java.
	 * lang.Integer, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GitLabSnippets createOrUpdateSippets(Integer id, String title, String fileName, String content,
			String description, String visibility) throws IOException {
		if (gitLabAPIV4 == null) {
			return null;
		}
		if (id == null) {
			return gitLabAPIV4.createSnippets(title, fileName, content, description, "public");
		} else {
			return gitLabAPIV4.updateSnippets(id, title, fileName, content, description, "public");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.eclipse.code.review.gitlab.IGitLabApiWrapper#createOrUpdateIssue(int,
	 * int, int, int, java.lang.String, java.lang.String, java.lang.String,
	 * org.gitlab.api.models.GitlabIssue.Action)
	 */
	@Override
	public GitlabIssue createOrUpdateIssue(Integer issueId, Integer assigneeId, Integer milestoneId,
			String labels, String description, String title, Action action) throws IOException {
		if (gitLabProject == null || gitLabAPIV4 == null) {
			return null;
		}
		Integer projectId = gitLabProject.getId();
		if (assigneeId == null) {
			assigneeId = 0;
		}
		if (milestoneId == null) {
			milestoneId = 0;
		}
		if (issueId == null) {
			return gitLabAPIV4.createIssue(projectId, assigneeId, milestoneId, labels, description, title);
		} else {
			return gitLabAPIV4.editIssue(projectId, issueId, assigneeId, milestoneId, labels,
					description, title, action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.eclipse.code.review.gitlab.IGitLabApiWrapper#getCurrentUserInfo()
	 */
	@Override
	public GitlabUser getCurrentUserInfo() {
		try {
			return gitLabAPIV4.getUser();
		} catch (IOException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see cn.eclipse.code.review.gitlab.IGitLabApiWrapper#createIssueNote(org.gitlab.api.models.GitlabIssue, java.lang.String)
	 */
	@Override
	public GitlabNote createIssueNote(GitlabIssue issue, String message) {
		try {
			return gitLabAPIV4.createNote(issue, message);
		} catch (IOException e) {
			return null;
		}
	}

//	public static void main(String[] args) throws IOException {
//		GitlabAPI gitLabAPIV4 = GitlabAPI.connect("http://192.168.1.105:10000", "k3TqSxwqrPy_-tCdKRSf");
//		List<GitlabCommitDiff> commitDiffs = gitLabAPIV4.getAllCommitDiffs(5,
//				"d9e34ee12054f8b78adbe8599baf64fd3143bec3");
//		System.out.println("-----------:" + commitDiffs);
//	}

}
