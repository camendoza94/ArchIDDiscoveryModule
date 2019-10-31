package archtoring.utils;

import java.util.UUID;

public class Issue {
	private Integer rule;
	private String description;
	private String id;

	public Issue(Integer rule, String description) {
		super();
		this.rule = rule;
		this.description = description;
		this.id = UUID.randomUUID().toString();
	}

	public Integer getRule() {
		return rule;
	}

	public void setRule(Integer rule) {
		this.rule = rule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
