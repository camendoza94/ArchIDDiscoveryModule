package archtoring.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;

public class GithubHandler {
	public static String[] output;
	public static IssueService service;
	public static List<Issue> issues;
	public static RepositoryId repo;

	public GithubHandler() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", "git remote get-url origin && git rev-parse HEAD");

			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			output = new String[2];
			int index = 0;
			while ((line = reader.readLine()) != null) {
				output[index] = line;
				index++;
			}
			process.waitFor();
			service = new IssueService();
			service.getClient().setOAuth2Token("cc0547bb556cb27747ad7876b8401f81c787b2cb");
			String url = output[0];
			String org = url.split("/")[3];
			String repoName = url.split("/")[4];
			if(repoName.endsWith(".git"))
				repoName = repoName.substring(0, repoName.indexOf("."));
			repo = new RepositoryId(org, repoName);
			HashMap<String, String> options = new HashMap<String, String>();
			options.put("state", "all");
			issues = service.getIssues(repo, options);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void execute() {
		for (Issue i : issues) {
			if (i.getState().equals("open")) {
				List<Label> previousLabels = i.getLabels();
				boolean found = false;
				for (int j = 0; j < previousLabels.size() && !found; j++) {
					if (previousLabels.get(j).getName().equals(ModelHandler.getArgs().get(0)))
						found = true;
				}
				if (!found) {
					i.setState("closed");
					try {
						service.editIssue(repo, i);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}