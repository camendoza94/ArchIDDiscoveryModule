package archtoring.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.GithubHandler;

public class IssuesTool extends AbstractTool {

	public void addIssueOnGithub(int id, String action, String className, String element, String severity,
			String path) {
		GithubHandler.issuesCount[id - 1] = ++GithubHandler.issuesCount[id - 1];
		if (GithubHandler.fileIssuesCount.containsKey(path)) {
			int[] old = GithubHandler.fileIssuesCount.get(path);
			old[id - 1] = ++old[id - 1];
			GithubHandler.fileIssuesCount.put(path, old);
		} else {
			int[] array = new int[18];
			array[id - 1] = 1;
			GithubHandler.fileIssuesCount.put(path, array);
		}
	}

	public void addDependency(String from, String to) {
		if (GithubHandler.dependencies.containsKey(from)) {
			List<String> old = GithubHandler.dependencies.get(from);
			old.add(to);
		} else {
			ArrayList<String> dep = new ArrayList<String>();
			dep.add(to);
			GithubHandler.dependencies.put(from, dep);
		}
		
		if (GithubHandler.dependenciesIn.containsKey(to)) {
			List<String> old = GithubHandler.dependenciesIn.get(to);
			old.add(from);
		} else {
			ArrayList<String> dep = new ArrayList<String>();
			dep.add(from);
			GithubHandler.dependenciesIn.put(to, dep);
		}
	}
}
