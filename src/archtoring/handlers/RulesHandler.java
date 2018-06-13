package archtoring.handlers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

public class RulesHandler extends AbstractHandler {

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
					Shell shell = HandlerUtil.getActiveShell(event);
					FileDialog dialog = new FileDialog(shell);
					dialog.setText("Select Rules File");
					String[] extensions = { "*.csv" };
					dialog.setFilterExtensions(extensions);
					if (dialog.open() != null) {
						String filename = dialog.getFileName();
						if (filename == "")
							System.out.println("You didn't select any file.");
						else
							System.out.println("You chose " + dialog.getFilterPath() + "\\" + filename);
						// TODO Extract data from CSV
						// TODO Use data to create model
						EcoreResourceFactoryImpl rs = new EcoreResourceFactoryImpl();
						Bundle bundle = Platform.getBundle("co.edu.uniandes.archtoring");
						URL fileURL = bundle.getEntry("egl/rules.ecore");

						try {
							Resource res = rs.createResource(URI.createFileURI(FileLocator.resolve(fileURL).getFile()));
							res.load(null);
							EPackage metapackage = (EPackage) res.getContents().get(0);
							EFactory employeeFactoryInstance = metapackage.getEFactoryInstance();
							EClass ruleClass = (EClass) metapackage.getEClassifier("Rule");
							EObject ruleObject = employeeFactoryInstance.create(ruleClass);
							EAttribute ruleTitle = ruleClass.getEAllAttributes().get(0);
							EAttribute ruleAction = ruleClass.getEAllAttributes().get(1);
							EAttribute ruleDebt = ruleClass.getEAllAttributes().get(2);
							ruleObject.eSet(ruleTitle, "TITLE_PLUGIN");
							ruleObject.eSet(ruleAction, "ACTION_PLUGIN");
							ruleObject.eSet(ruleDebt, 2);
							System.out.println(ruleObject.eGet(ruleTitle));
							ResourceSet resourseSet = new ResourceSetImpl();
							resourseSet.getPackageRegistry().put(metapackage.getNsURI(), metapackage);
							resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",
									new XMIResourceFactoryImpl());
							Resource resource = resourseSet.createResource(URI.createURI(project.getResource().getLocationURI() + "/archtoring/RulesModel.xmi"));
							resource.getContents().add(ruleObject);
							Map<String, Boolean> options = new HashMap<String, Boolean>();
							options.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
							resource.save(options);
						} catch (IOException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						// TODO ? Run EGL templates
					}
				}
			}
		}
		return null;
	}

}
