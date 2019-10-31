package archtoring.utils;

import java.util.List;

public class Decision {

	private String title;
	private String qa;
	private List<Rule> rules;

	public Decision(String title, String qa) {
		this.title = title;
		this.qa = qa;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQa() {
		return qa;
	}

	public void setQa(String qa) {
		this.qa = qa;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

}
