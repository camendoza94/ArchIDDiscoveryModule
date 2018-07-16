package archtoring.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

import archtoring.utils.EglStandalone;
import archtoring.utils.EpsilonStandalone;
import archtoring.wizard.SonarQubeProject;

public class PluginHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject)
					project = (IProject) element;
				else if (element instanceof IAdaptable)
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				if (project != null) {
					try {
						addToProjectStructure(project);
						runEGLTemplate(project);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	private static void addToProjectStructure(IProject newProject) throws CoreException {
    	Bundle bundle = Platform.getBundle("co.edu.uniandes.archtoring");
		URL fileURL = bundle.getEntry("egl/output");
        IFolder etcFolders;
		try {
			String output = FileLocator.resolve(fileURL).getPath();
			etcFolders = newProject.getFolder("/plugin");
	        copyFiles(new File(output), etcFolders); 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private static void copyFiles (File srcFolder, IFolder destFolder) {
    	try {
    		if(!destFolder.exists())
    			destFolder.create(IResource.NONE, true, null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
        for (File f: srcFolder.listFiles()) {
            if (f.isDirectory()) {
                IFolder newFolder = destFolder.getFolder(new Path(f.getName()));
                try {
					newFolder.create(true, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
                copyFiles(f, newFolder);
            } else {
                IFile newFile = destFolder.getFile(new Path(f.getName()));
                try {
					newFile.create(new FileInputStream(f), true, null);
				} catch (FileNotFoundException | CoreException e) {
					e.printStackTrace();
				}
            }
        }
    }
	
	private void runEGLTemplate(IProject project) {
		try {
			EglStandalone eol = new EglStandalone();
			eol.setSource("egl/generation.egl");
			List<IModel> models = new ArrayList<IModel>();
			models.add(EpsilonStandalone.createEmfModel("Model", project.getFullPath() + SonarQubeProject.ARCHTORING_RULES_MODEL_XMI,
					"egl/rules.ecore", true, true));
			eol.setModels(models);
			eol.setLocation(project.getLocation().toString() + "/plugin");
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
}

