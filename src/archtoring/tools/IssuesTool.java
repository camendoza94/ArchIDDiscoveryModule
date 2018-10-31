package archtoring.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.epsilon.eol.tools.AbstractTool;

public class IssuesTool extends AbstractTool{
	
	private Map<Integer, String> titles = new HashMap<Integer, String>();

	protected String name;
	
	public IssuesTool() {
		titles.put(1, "Implement Serializable in this DTO.");
		titles.put(2, "Add empty constructor for serializing.");
		titles.put(3, "Remove or edit no-serializable fields.");
		titles.put(4, "Add missing getter or setter.");
		titles.put(5, "Check constructor from Entity implementation in DTO.");
		titles.put(6, "Check toEntity implementation in DTO.");
		titles.put(7, "Remove fields that are not of type DTO or List.");
		titles.put(8, "Check constructor from Entity implementation in DetailDTO.");
		titles.put(9, "Check toEntity implementation in DetailDTO.");
		titles.put(10, "Add missing Path annotation on Resource.");
		titles.put(11, "Add missing Consumes annotation on Resource.");
		titles.put(12, "Add missing Produces annotation on Resource.");
		titles.put(13, "Add missing logic injection on Resource.");
		titles.put(14, "Add missing Stateless annotation on Logic.");
		titles.put(15, "Add missing persistence injection on Logic.");
		titles.put(16, "Add missing check for nullity and WebApplicationException.");
	}
	  
	public void setName(String name) {
		this.name = name;
	}
	  
	public String getName() {
		return name;
	}	  
	public String sayHello() {
	    return "Hello " + name;
	}
	
	public void addIssueOnGithub(int id, String action, String className, String description, String nonCompliant, String solution, String element, String severity) {
	        try {
	            IssueService service = new IssueService();
	            //TODO Change to auth for whole application
	            service.getClient().setOAuth2Token("cc0547bb556cb27747ad7876b8401f81c787b2cb");
	            //TODO Obtain repository info from git files
	            RepositoryId repo = new RepositoryId("Archtoring-ISIS2603201802", "s4_PartyServices");
	            List<Issue> issues = service.getIssues(repo, null);
	            String title = className + ".java - " + action ;
	            for (Issue i : issues) {
	                if (i.getTitle().equals(title)) {
	                	List<Label> previousLabels = i.getLabels();
	                	Label newLabel = new Label();
	                	newLabel.setName("C3");
	                	newLabel.setColor("#4fa008");
	                	previousLabels.add(newLabel);
	                	i.setLabels(previousLabels);
	                	service.editIssue(repo, i);
	                	return;
	                }
	            }
	            Issue issue = new Issue();
	            issue.setTitle(title);
	            issue.setBody("<h2>Issue: " + action + "</h2>"
	            		+ "<p>Found on file</p>"
	            		+ "<p>Go to the <a href='archtoringkb.herokuapp.com'>Knowledge Base</a> to find more info about this violation");
	            
	            List<Label> labels = new ArrayList<Label>();
	            
	            Label elementLabel = new Label();
	            elementLabel.setName(element);
	            elementLabel.setColor("#333dcc");
	            labels.add(elementLabel);

	            //TODO Get version from pom.xml to tag issue release?
	            Label releaseLabel = new Label();
	            releaseLabel.setName("C3");
	            releaseLabel.setColor("#4fa008");
	            labels.add(releaseLabel);
	            
	            Label ruleLabel = new Label();
	            ruleLabel.setName("R" + id);
	            ruleLabel.setColor("#e04ac7");
	            labels.add(ruleLabel);
	            
	            Label severityLabel = new Label();
	            severityLabel.setName(severity);
	            severityLabel.setColor("#e04ac7");
	            labels.add(severityLabel);
	            
	            issue.setLabels(labels);
	            service.createIssue(repo, issue);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
