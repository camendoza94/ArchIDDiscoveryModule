package archtoring.utils;

public class Rule {
	private String title;
	private String severity;
	private String category;
	private Integer id;

	public Rule(String title, String severity, String category, Integer id) {
		this.title = title;
		this.severity = severity;
		this.category = category;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
