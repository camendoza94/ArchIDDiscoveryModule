package archtoring.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.GithubHandler;
import archtoring.utils.Decision;
import archtoring.utils.Issue;
import archtoring.utils.Rule;

public class IssuesTool extends AbstractTool {

	public void addIssue(int id, String path, String description) {
		GithubHandler.issuesCount[id - 1] = ++GithubHandler.issuesCount[id - 1];
		if (GithubHandler.fileIssuesCount.containsKey(path)) {
			int[] old = GithubHandler.fileIssuesCount.get(path);
			old[id - 1] = ++old[id - 1];
			GithubHandler.fileIssuesCount.put(path, old);
		} else {
			int[] array = new int[22];
			array[id - 1] = 1;
			GithubHandler.fileIssuesCount.put(path, array);
		}
		Issue i = new Issue(id, description);
		if (GithubHandler.issues.containsKey(path)) {
			List<Issue> old = GithubHandler.issues.get(path);
			old.add(i);
		} else {
			ArrayList<Issue> issues = new ArrayList<Issue>();
			issues.add(i);
			GithubHandler.issues.put(path, issues);
		}
	}

	public void addDependency(String from, String to) {
		if (GithubHandler.dependencies.containsKey(from)) {
			Set<String> old = GithubHandler.dependencies.get(from);
			old.add(to);
		} else {
			Set<String> dep = new HashSet<String>();
			dep.add(to);
			GithubHandler.dependencies.put(from, dep);
		}
		
		if (GithubHandler.dependenciesIn.containsKey(to)) {
			Set<String> old = GithubHandler.dependenciesIn.get(to);
			old.add(from);
		} else {
			Set<String> dep = new HashSet<String>();
			dep.add(from);
			GithubHandler.dependenciesIn.put(to, dep);
		}
	}
	
	public void addCategory(String title, String qa, String rule, String severity, String category, int id, int debt,
			String description, String solution, String example) {
		Decision d = new Decision(title, qa);
		Rule r = new Rule(rule, severity, category, debt, solution, example, description, id);
		Decision d2 = exists(d);
		if (d2 != null) {
			List<Rule> old = GithubHandler.decisions.get(d2);
			old.add(r);
		} else {
			ArrayList<Rule> rules = new ArrayList<Rule>();
			rules.add(r);
			GithubHandler.decisions.put(d, rules);
		}
	}

	private Decision exists(Decision d) {
		Object[] keyset = GithubHandler.decisions.keySet().toArray();
		for (int i = 0; i < keyset.length; i++) {
			if (((Decision) keyset[i]).getTitle().equals(d.getTitle()))
				return (Decision) keyset[i];
		}
		return null;
	}
}
