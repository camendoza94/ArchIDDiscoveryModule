package archtoring.sirius;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.action.AbstractExternalJavaAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class ExternalJavaAction extends AbstractExternalJavaAction{

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
		dialog.setText("Select Rules File");
		String[] extensions = { "*.csv" };
		dialog.setFilterExtensions(extensions);		
	}

}
