package archtoring.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

import archtoring.utils.CSVUtils;
import archtoring.utils.EglStandalone;
import archtoring.utils.EpsilonStandalone;

public class RulesHandler extends AbstractHandler {

	private static final String ARCHTORING_RULES_MODEL_XMI = "/archtoring/RulesModel.xmi";

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
						else {
							System.out.println("You chose " + dialog.getFilterPath() + "\\" + filename);
							createModel(project, dialog.getFilterPath() + "\\" + filename);
							runEGLTemplate(project);
						}
					}
				}
			}
		}
		return null;
	}

	private void runEGLTemplate(IJavaProject project) {
		try {
			EglStandalone eol = new EglStandalone();
			eol.setSource("egl/generation.egl");
			List<IModel> models = new ArrayList<IModel>();
			models.add(EpsilonStandalone.createEmfModel("Model", project.getResource().getFullPath() + ARCHTORING_RULES_MODEL_XMI,
					"egl/rules.ecore", true, true));
			eol.setModels(models);
			eol.setLocation(project.getResource().getLocation().toString() + "/plugin");
			eol.execute(true);
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createModel(IJavaProject project, String filename) {
		EcoreResourceFactoryImpl rs = new EcoreResourceFactoryImpl();
		Bundle bundle = Platform.getBundle("co.edu.uniandes.archtoring");
		URL fileURL = bundle.getEntry("egl/rules.ecore");

		try {
			// Obtain meta-model
			Resource res = rs.createResource(URI.createFileURI(FileLocator.resolve(fileURL).getFile()));
			res.load(null);
			EPackage metapackage = (EPackage) res.getContents().get(0);
			EFactory employeeFactoryInstance = metapackage.getEFactoryInstance();
			EClass ruleClass = (EClass) metapackage.getEClassifier("Rule");
			EAttribute ruleTitle = ruleClass.getEAllAttributes().get(0);
			EAttribute ruleAction = ruleClass.getEAllAttributes().get(1);
			EAttribute ruleDebt = ruleClass.getEAllAttributes().get(2);
			EAttribute ruleSeverity = ruleClass.getEAllAttributes().get(3);
			EAttribute ruleDescription = ruleClass.getEAllAttributes().get(4);
			EAttribute ruleNonCompliantExample = ruleClass.getEAllAttributes().get(5);
			EAttribute ruleCompliantSolution = ruleClass.getEAllAttributes().get(6);
			EAttribute ruleID = ruleClass.getEAllAttributes().get(7);

			// Create model
			ResourceSet resourseSet = new ResourceSetImpl();
			resourseSet.getPackageRegistry().put(metapackage.getNsURI(), metapackage);
			resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
			Resource resource = resourseSet
					.createResource(URI.createURI(project.getResource().getLocationURI() + ARCHTORING_RULES_MODEL_XMI));

			Scanner scanner = new Scanner(new File(filename));
			if (scanner.hasNextLine())
				scanner.nextLine(); // Skip headers
			else {
				// TODO Show message empty CSV
			}
			while (scanner.hasNext()) {
				// Create Rule object
				List<String> line = CSVUtils.parseLine(scanner.nextLine());
				EObject ruleObject = employeeFactoryInstance.create(ruleClass);
				ruleObject.eSet(ruleID, Integer.parseInt(line.get(0)));
				ruleObject.eSet(ruleTitle, line.get(1));
				ruleObject.eSet(ruleAction, line.get(2));
				ruleObject.eSet(ruleDebt, Integer.parseInt(line.get(3)));
				ruleObject.eSet(ruleSeverity, line.get(4));
				ruleObject.eSet(ruleDescription, line.get(5));
				ruleObject.eSet(ruleNonCompliantExample, line.get(6));
				ruleObject.eSet(ruleCompliantSolution, line.get(7));
				// Add new Rule to model
				resource.getContents().add(ruleObject);
			}
			scanner.close();
			Map<String, Boolean> options = new HashMap<String, Boolean>();
			options.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			resource.save(options);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
