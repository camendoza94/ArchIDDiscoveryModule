package archtoring.tools;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.DataHandler;
import archtoring.utils.Decision;
import archtoring.utils.Rule;

public class IssuesTool extends AbstractTool {

	public void addIssue(int id, String path) {
		DataHandler.issuesCount[id - 1] = ++DataHandler.issuesCount[id - 1];
		if (DataHandler.fileIssuesCount.containsKey(path)) {
			int[] old = DataHandler.fileIssuesCount.get(path);
			old[id - 1] = ++old[id - 1];
			DataHandler.fileIssuesCount.put(path, old);
		} else {
			int[] array = new int[11];
			array[id - 1] = 1;
			DataHandler.fileIssuesCount.put(path, array);
		}
	}

	public void addDependency(String from, String to) {
		if (DataHandler.dependencies.containsKey(from)) {
			List<String> old = DataHandler.dependencies.get(from);
			old.add(to);
		} else {
			ArrayList<String> dep = new ArrayList<String>();
			dep.add(to);
			DataHandler.dependencies.put(from, dep);
		}

		if (DataHandler.dependenciesIn.containsKey(to)) {
			List<String> old = DataHandler.dependenciesIn.get(to);
			old.add(from);
		} else {
			ArrayList<String> dep = new ArrayList<String>();
			dep.add(from);
			DataHandler.dependenciesIn.put(to, dep);
		}
	}

	public void addCategory(String title, String qa, String rule, String severity, String category, int id) {
		Decision d = new Decision(title, qa);
		Rule r = new Rule(rule, severity, category, id);
		Decision d2 = exists(d);
		if (d2 != null) {
			List<Rule> old = DataHandler.decisions.get(d2);
			old.add(r);
		} else {
			ArrayList<Rule> rules = new ArrayList<Rule>();
			rules.add(r);
			DataHandler.decisions.put(d, rules);
		}
	}

	private Decision exists(Decision d) {
		Object[] keyset = DataHandler.decisions.keySet().toArray();
		for (int i = 0; i < keyset.length; i++) {
			if (((Decision) keyset[i]).getTitle().equals(d.getTitle()))
				return (Decision) keyset[i];
		}
		return null;
	}
}
