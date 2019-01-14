/**
 * @desciption
 *
 * @author jack_fan
 * @date 2019年1月1日
 */
package cn.eclipse.code.review.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @desciption 仓库提供者
 * @author jack_fan
 * @date 2019年1月1日
 */
public class CodeReviewRepositoryProvider {
	// 仓库.git文件
	private static final String GIT = ".git";

	/**
	 * @desciption 获取git提交commitSHA
	 * @author jack_fan
	 * @date 2019年1月4日
	 * @param filePath
	 * @return git提交commitSHA列表
	 */
	public static List<String> getCommitSHAList(String filePath) {
		if (filePath == null) {
			return null;
		}
		String repoPath = findRepoPath(filePath);
		if (repoPath == null) {
			return null;
		} else {
			Git git = null;
			try {
				Repository repo = openRepository(new File(repoPath));
				git = new Git(repo);
				List<String> commitSHA = new ArrayList<>();
				Iterable<RevCommit> gitlog = git.log().call();
                for (RevCommit revCommit : gitlog) {
					// 版本号
					String version = revCommit.getName();
					commitSHA.add(version);
				}
				return commitSHA;
			} catch (IOException | GitAPIException e) {
				e.printStackTrace();
			} finally {
				if (git != null) {
					git.close();
				}
			}
		}
		return null;
	}

	/**
	 * @desciption 通过当前文件路径寻找git仓库路径， 寻找规则：从文件所在目录往上遍历，找到.git文件夹路径
	 * @author jack_fan
	 * @date 2019年1月3日
	 * @return .git文件夹路径
	 */
	public static String findGitRepositoryByProjectFilePath(String filePath) {
		if (filePath == null) {
			return null;
		}
		String repoPath = findRepoPath(filePath);
		if (repoPath == null) {
			return null;
		} else {
			try {
				Repository repo = openRepository(new File(repoPath));
				String url = repo.getConfig().getString("remote", "origin", "url");
				return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".git"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * @desciption 查询.git仓库位置
	 * @author jack_fan
	 * @date 2019年1月4日
	 * @param CodeFilePath
	 * @return
	 */
	private static String findRepoPath(String CodeFilePath) {
		if (CodeFilePath == null) {
			return null;
		}
		boolean isFile = false;
		File file = new File(CodeFilePath);
		if (file.isFile()) {
			isFile = true;
			file = file.getParentFile();
		}
		if (file == null) {
			return null;
		}
		String parentDir = file.getAbsolutePath();
		if (!isFile) {
			if (file.getParentFile() != null) {
				parentDir = file.getParentFile().getAbsolutePath();
			} else {
				parentDir = null;
			}
		}
		File[] files = file.listFiles();
		if (files == null) {
			return null;
		}
		for (File item : files) {
			if (item.getName().endsWith(GIT)) {
				return item.getAbsolutePath();
			}
		}
		System.out.println("遍历的层级：" + parentDir);
		return findRepoPath(parentDir);
	}

	/**
	 * @desciption 打开git reporistory
	 * @author jack_fan
	 * @date 2019年1月4日
	 * @param repoDir
	 * @return
	 * @throws IOException
	 */
	private static Repository openRepository(File repoDir) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(repoDir).readEnvironment().findGitDir().build();
		return repository;
	}
}
