package archtoring.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class GenerationHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IFile file = null;
				if (element instanceof IJavaProject)
					file = (IFile) element;
				else if (element instanceof IAdaptable)
					file = (IFile) ((IAdaptable) element).getAdapter(IFile.class);
				if (file != null) {
					GenerateJavaExtended javaGenerator;
					try {
						Shell shell = HandlerUtil.getActiveShell(event);
						DirectoryDialog dialog = new DirectoryDialog(shell);
						dialog.setText("Select folder to save source");
						if (dialog.open() != null) {
							ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
							ILaunchConfigurationType type = manager
									.getLaunchConfigurationType("org.epsilon.eol.eclipse.dt.launching.EolLaunchConfigurationDelegate");
							ILaunchConfiguration[] lcs = manager.getLaunchConfigurations(type);
							for (ILaunchConfiguration iLaunchConfiguration : lcs) {
								if (iLaunchConfiguration.getName().equals("comments")) {
									ILaunchConfigurationWorkingCopy t = iLaunchConfiguration.getWorkingCopy();
									List<String> models = t.getAttribute("models", new ArrayList<String>());
									String entry = models.get(0);
									int fileStart = entry.indexOf("modelFile=");
									int fileEnd = entry.indexOf("expand=", fileStart);
									String substringFile = entry.substring(fileStart + "modelFile=".length(), fileEnd);
									String newEntry = entry.replace(substringFile, URI.createURI(file.getFullPath().toString()) + "\n");
									models.clear();
									models.add(newEntry);
									t.setAttribute("models", models);
									ILaunchConfiguration config = t.doSave();
									if (config != null) {
										config.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());
									}
								}
							}
							javaGenerator = new GenerateJavaExtended(URI.createFileURI(file.getFullPath().toString()),
									new File(dialog.getFilterPath()), new ArrayList<Object>());
							javaGenerator.doGenerate(null);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
