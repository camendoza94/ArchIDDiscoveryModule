package archtoring.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.tools.AbstractTool;

import archtoring.handlers.DataHandler;

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
}
