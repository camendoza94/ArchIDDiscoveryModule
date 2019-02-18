package archtoring.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.epsilon.eol.tools.AbstractTool;

public class IssuesTool extends AbstractTool{

	public void addIssueOnGithub(int id, String action, String className, String element, String severity, String path) {
	        try {
	            IssueService service = new IssueService();
	            //TODO Change to auth for whole application
	            service.getClient().setOAuth2Token("cc0547bb556cb27747ad7876b8401f81c787b2cb");
	            //TODO Obtain repository info from git files
	            String org = "Uniandes-isis2603";
	            String repoName = "s4_RequirementManagementSystem_201910";
	            //TODO Get commit info from git files
	            String commit = "060fe6e67377a96f0036eefa0fb711d218bc2873";
	            String backPath = "s4_requirement-back/src/main/java";
	            String frontPath = "s4_requirement-api/src/main/java";
	            String folder = id == 14 || id == 15 ? backPath : frontPath;
	            RepositoryId repo = new RepositoryId(org, repoName);
	            HashMap<String, String> options = new HashMap<String, String>();
	            options.put("state", "all");
	            List<Issue> issues = service.getIssues(repo, options);
	            String title = className + ".java - " + action ;
	            for (Issue i : issues) {
	                if (i.getTitle().equals(title)) {
	                	List<Label> previousLabels = i.getLabels();
	                	Label newLabel = new Label();
	                	newLabel.setName("C1");
	                	newLabel.setColor("4fa008");
	                	previousLabels.add(newLabel);
	                	i.setLabels(previousLabels);
	                	i.setState("open");
	                	service.editIssue(repo, i);
	                	return;
	                }
	            }
	            Issue issue = new Issue();
	            issue.setTitle(title);
	            issue.setBody("<h2>Issue: " + action + "</h2>"
	            		+ "<p>Found on file: <a href='https://github.com/" + org + "/" + repoName + "/blob/" + commit + "/" +  folder + "/" + path + "'>" + className + ".java" + "</a></p>"
	            		+ "<p>On commit:  <a href='https://github.com/" + org + "/" + repoName + "/tree/" + commit + "'>" + commit + "</a></p>"
	            		+ "<p>Go to the <a href='https://archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");
	            
	            List<Label> labels = new ArrayList<Label>();
	            
	            Label elementLabel = new Label();
	            elementLabel.setName(element);
	            elementLabel.setColor("333dcc");
	            labels.add(elementLabel);

	            //TODO Get version from pom.xml to tag issue release?
	            Label releaseLabel = new Label();
	            releaseLabel.setName("C1");
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
	            service.createIssue(repo, issue);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
