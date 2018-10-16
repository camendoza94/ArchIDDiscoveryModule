package archtoring.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.egit.github.core.Issue;
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
	
	public void addIssueOnGithub(String id, String module) {
	        String userName;
	        String repoName;
	        try {
	            IssueService service = new IssueService();
	            service.getClient().setOAuth2Token("cc0547bb556cb27747ad7876b8401f81c787b2cb");
	            RepositoryId repo = new RepositoryId("Archtoring-ISIS2603201802", "s2_Boletas");
	            List<Issue> issues = service.getIssues(repo, null);
	            /*String title = titles.get(id) + " - " + context.getFile().getParentFile().getName()
	                    + "/" + context.getFile().getName();*/
	            String title = id + " - " + module;
	            for (Issue i : issues) {
	                if (i.getTitle().equals(title))
	                    return;
	            }
	            Issue issue = new Issue();
	            issue.setTitle(title);
	            /*URL resource = RefactoringRule.class.getResource(RESOURCE_BASE_PATH + "/" + "R" + this.Id
	                    + "RefactoringRule" + "_java.html");*/
	        
	            issue.setBody("TEST");
	           
	            service.createIssue(repo, issue);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
