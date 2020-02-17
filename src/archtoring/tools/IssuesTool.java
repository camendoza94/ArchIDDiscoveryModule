package archtoring.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.egit.github.core.Label;
import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.EPLHandler;
import archtoring.handlers.GithubHandler;
import archtoring.handlers.ModelHandler;
import archtoring.utils.Decision;
import archtoring.utils.Issue;
import archtoring.utils.Rule;

public class IssuesTool extends AbstractTool {

	public void addIssue(int id, String path, String description) {
		if (EPLHandler.args.get(0).equals("--db") || EPLHandler.args.get(0).equals("--full")) {
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

		if (EPLHandler.args.get(0).equals("--gh") || EPLHandler.args.get(0).equals("--full")) {
			Rule r = getRule(id);
			try {
				String className = path.split("/")[path.split("/").length - 1];
				String title = className + " - " + r.getTitle();
				String url = GithubHandler.output[0];
				String org = url.split("/")[3];
				String repoName = url.split("/")[4];
				if (repoName.endsWith(".git"))
					repoName = repoName.substring(0, repoName.indexOf("."));
				String commit = GithubHandler.output[1];
				String[] names = ModelHandler.getNames();
				String backPath = names[0] + "/src/main/java";
				String frontPath = names[1] + "/src/main/java";
				String folder = (id == 14 || id == 15 || id > 18) ? backPath : frontPath;
				for (org.eclipse.egit.github.core.Issue i1 : GithubHandler.issuesGithub) {
					List<Label> previousLabels = i1.getLabels();
					if (i1.getTitle().equals(title)) {
						Label releaseLabel = new Label();
						releaseLabel.setName(commit.substring(0, 7));
						releaseLabel.setColor("4fa008");
						previousLabels.add(releaseLabel);
						i1.setBody("<h2>Issue: " + r.getTitle() + "</h2>"
								+ "<p>Found on file: <a href='https://github.com/" + org + "/" + repoName + "/blob/"
								+ commit + "/" + folder + "/" + path + "'>" + className + ".java" + "</a></p>"
								+ "<p>On commit:  <a href='https://github.com/" + org + "/" + repoName + "/tree/"
								+ commit + "'>" + commit + "</a></p>"
								+ "<p>Go to the <a href='https://archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");
						i1.setLabels(previousLabels);
						i1.setState("open");
						GithubHandler.service.editIssue(GithubHandler.repo, i1);
						return;
					}
				}
				org.eclipse.egit.github.core.Issue issue = new org.eclipse.egit.github.core.Issue();
				issue.setTitle(title);
				issue.setBody("<h2>Issue: " + r.getTitle() + "</h2>" + "<p>Found on file: <a href='https://github.com/"
						+ org + "/" + repoName + "/blob/" + commit + "/" + folder + "/" + path + "'>" + className
						+ ".java" + "</a></p>" + "<p>On commit:  <a href='https://github.com/" + org + "/" + repoName
						+ "/tree/" + commit + "'>" + commit + "</a></p>"
						+ "<p>Go to the <a href='https://archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");

				if (GithubHandler.author != null)
					issue.setAssignee(GithubHandler.author);

				List<Label> labels = new ArrayList<Label>();

				Label categoryLabel = new Label();
				categoryLabel.setName(r.getCategory());
				categoryLabel.setColor("4fa008");
				labels.add(categoryLabel);

				Label releaseLabel = new Label();
				releaseLabel.setName(commit.substring(0, 7));
				releaseLabel.setColor("4fa008");
				labels.add(releaseLabel);

				Label ruleLabel = new Label();
				ruleLabel.setName("R" + id);
				ruleLabel.setColor("e04ac7");
				labels.add(ruleLabel);

				Label severityLabel = new Label();
				severityLabel.setName(r.getSeverity());
				severityLabel.setColor("e04ac7");
				labels.add(severityLabel);
				issue.setLabels(labels);
				GithubHandler.service.createIssue(GithubHandler.repo, issue);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			String description, String solution, String example, String element) {
		Decision d = new Decision(title, qa);
		Rule r = new Rule(rule, severity, category, debt, solution, example, description, id, element);
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

	private Rule getRule(int id) {
		ArrayList<List<Rule>> rulesArray = new ArrayList<List<Rule>>(GithubHandler.decisions.values());
		for (int i = 0; i < rulesArray.size(); i++) {
			List<Rule> list = rulesArray.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getId() == id)
					return list.get(j);
			}
		}
		return null;
	}
}
