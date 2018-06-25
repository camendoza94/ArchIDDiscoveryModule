package archtoring.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.IStructuredSelection;

public class EPLHandler extends AbstractHandler {

	private static final String[] FILES = { "/Archtoring/epl/dtos.epl", "/Archtoring/epl/detailDtos.epl",
			"/Archtoring/epl/logic.epl", "/Archtoring/epl/resources.epl" };

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IJavaProject project = null;
				if (element instanceof IJavaProject)
					project = (IJavaProject) element;
				else if (element instanceof IAdaptable)
					project = (IJavaProject) ((IAdaptable) element).getAdapter(IJavaProject.class);
				if (project != null) {
					try {
						ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
						ILaunchConfigurationType type = manager.getLaunchConfigurationType(
								"org.epsilon.epl.eclipse.dt.launching.EplLaunchConfigurationDelegate");
						ILaunchConfiguration[] lcs = manager.getLaunchConfigurations(type);
						for (ILaunchConfiguration iLaunchConfiguration : lcs) {
							if (iLaunchConfiguration.getName().equals("EPL")) {
								for (String eplFile : FILES) {
									ILaunchConfigurationWorkingCopy t = iLaunchConfiguration.getWorkingCopy();
									t.setAttribute("source", eplFile);
									List<String> models = t.getAttribute("models", new ArrayList<String>());
									String entry = models.get(0);
									int fileStart = entry.indexOf("modelFile=");
									int fileEnd = entry.indexOf("expand=", fileStart);
									String substringFile = entry.substring(fileStart + "modelFile=".length(), fileEnd);
									String newEntry = entry.replace(substringFile, URI.createURI(project.getResource().getFullPath() + "/" + project.getElementName() + "_java.xmi") + "\n");
									models.clear();
									models.add(newEntry);
									t.setAttribute("models", models);
									ILaunchConfiguration config = t.doSave();
									if (config != null) {
										config.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());
									}
								}
							}
						}
					} catch (CoreException ex) {
						System.out.println("Error while searching LaunchConfig");
					}
				}
			}
		}
		return null;
	}
}
