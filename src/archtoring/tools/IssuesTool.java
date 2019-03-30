package archtoring.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.GithubHandler;
import archtoring.handlers.ModelHandler;

public class IssuesTool extends AbstractTool {

	public void addIssueOnGithub(int id, String action, String className, String element, String severity,
			String path) {
		try {
			String title = className + ".java - " + action;
			List<String> commands = ModelHandler.getArgs();
			String releaseNumber = commands.get(0);
			String url = GithubHandler.output[0];
			String org = url.split("/")[3];
			String repoName = url.split("/")[4];
			if(repoName.endsWith(".git"))
				repoName = repoName.substring(0, repoName.indexOf("."));
			String commit = GithubHandler.output[1];
			String[] names = ModelHandler.getNames();
			String backPath = names[0] + "/src/main/java";
			String frontPath = names[1] + "/src/main/java";
			String folder = id == 14 || id == 15 ? backPath : frontPath;
			for (Issue i : GithubHandler.issues) {
				List<Label> previousLabels = i.getLabels();
				if (i.getTitle().equals(title)) {
					Label newLabel = new Label();
					newLabel.setName(releaseNumber);
					newLabel.setColor("4fa008");
					previousLabels.add(newLabel);
					i.setBody("<h2>Issue: " + action + "</h2>" + "<p>Found on file: <a href='https://github.com/" + org
							+ "/" + repoName + "/blob/" + commit + "/" + folder + "/" + path + "'>" + className + ".java"
							+ "</a></p>" + "<p>On commit:  <a href='https://github.com/" + org + "/" + repoName + "/tree/"
							+ commit + "'>" + commit + "</a></p>"
							+ "<p>Go to the <a href='https://archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");
					i.setLabels(previousLabels);
					i.setState("open");
					GithubHandler.service.editIssue(GithubHandler.repo, i);
					return;
				}
			}
			Issue issue = new Issue();
			issue.setTitle(title);
			issue.setBody("<h2>Issue: " + action + "</h2>" + "<p>Found on file: <a href='https://github.com/" + org
					+ "/" + repoName + "/blob/" + commit + "/" + folder + "/" + path + "'>" + className + ".java"
					+ "</a></p>" + "<p>On commit:  <a href='https://github.com/" + org + "/" + repoName + "/tree/"
					+ commit + "'>" + commit + "</a></p>"
					+ "<p>Go to the <a href='https://archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");
			if(GithubHandler.author != null)
				issue.setAssignee(GithubHandler.author);
			List<Label> labels = new ArrayList<Label>();

			Label elementLabel = new Label();
			elementLabel.setName(element);
			elementLabel.setColor("333dcc");
			labels.add(elementLabel);

			Label releaseLabel = new Label();
			releaseLabel.setName(releaseNumber);
			releaseLabel.setColor("4fa008");
			labels.add(releaseLabel);

			Label ruleLabel = new Label();
			ruleLabel.setName("R" + id);
			ruleLabel.setColor("e04ac7");
			labels.add(ruleLabel);

			Label severityLabel = new Label();
			severityLabel.setName(severity);
			severityLabel.setColor("e04ac7");
			labels.add(severityLabel);

			issue.setLabels(labels);
			GithubHandler.service.createIssue(GithubHandler.repo, issue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
