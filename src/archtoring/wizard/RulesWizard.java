package archtoring.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class RulesWizard extends Wizard implements INewWizard {

	public RulesWizard() {
		 super();
	     setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		addPage(new UMLWizardPage());
	}

}
