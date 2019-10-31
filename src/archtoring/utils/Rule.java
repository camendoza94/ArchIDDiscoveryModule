package archtoring.utils;

public class Rule {
	private String title;
	private String severity;
	private String category;
	private Integer debt;
	private String compliantSolution;
	private String nonCompliantExample;
	private String description;
	private Integer id;

	public Rule(String title, String severity, String category, Integer debt, String compliantSolution,
			String nonCompliantExample, String description, Integer id) {
		super();
		this.title = title;
		this.severity = severity;
		this.category = category;
		this.debt = debt;
		this.compliantSolution = compliantSolution;
		this.nonCompliantExample = nonCompliantExample;
		this.description = description;
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

	public Integer getDebt() {
		return debt;
	}

	public void setDebt(Integer debt) {
		this.debt = debt;
	}

	public String getCompliantSolution() {
		return compliantSolution;
	}

	public void setCompliantSolution(String compliantSolution) {
		this.compliantSolution = compliantSolution;
	}

	public String getNonCompliantExample() {
		return nonCompliantExample;
	}

	public void setNonCompliantExample(String nonCompliantExample) {
		this.nonCompliantExample = nonCompliantExample;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
