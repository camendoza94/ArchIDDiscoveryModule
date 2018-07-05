package archtoring.wizard;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class RulesWizard extends Wizard implements INewWizard {

	private UMLWizardPage pageOne;

	public RulesWizard() {
		 super();
	     setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		String name = pageOne.getProjectName();
		URI location = pageOne.getLocationURI();
		SonarQubeProject.createProject(name, location);
		return true;
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		pageOne = new UMLWizardPage();
		addPage(pageOne);
	}

}
