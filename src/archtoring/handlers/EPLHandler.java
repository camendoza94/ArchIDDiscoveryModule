package archtoring.handlers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class EPLHandler extends AbstractHandler {
	
	private static final String[] FILES = {"/Archtoring/epl/dtos.epl", "/Archtoring/epl/detailDtos.epl", "/Archtoring/epl/logic.epl", "/Archtoring/epl/resources.epl"};

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IFile file = null;
				if (element instanceof IFile)
					file = (IFile) element;
				else if (element instanceof IAdaptable)
					file = (IFile) ((IAdaptable) element).getAdapter(IFile.class);
				if (file != null) {
					try {
						ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
						ILaunchConfigurationType type = manager.getLaunchConfigurationType(
								"org.epsilon.epl.eclipse.dt.launching.EplLaunchConfigurationDelegate");
						ILaunchConfiguration[] lcs = manager.getLaunchConfigurations(type);
						System.out.println(lcs.length);
						for (ILaunchConfiguration iLaunchConfiguration : lcs) {
							if (iLaunchConfiguration.getName().equals("EPL")) {
								for (String eplFile : FILES) {
									ILaunchConfigurationWorkingCopy t = iLaunchConfiguration.getWorkingCopy();
									t.setAttribute("source", eplFile);
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
