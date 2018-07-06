package archtoring.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.UMLPackage;
import archtoring.wizard.SonarQubeProjectNature;;

public class SonarQubeProject {
	/**
	 * For this marvelous project we need to: - create the default Eclipse
	 * project - add the custom project nature - create the folder structure
	 *
	 * @param projectName
	 * @param location
	 * @param location
	 * @param natureId
	 * @return
	 */
	public static IProject createProject(String projectName, URI location) {
		Assert.isNotNull(projectName);
		Assert.isTrue(projectName.trim().length() > 0);

		IProject project = createBaseProject(projectName, null);
		try {
			addNature(project);
			String[] paths = {"epl" , "model", "rules", "plugin"};
			addToProjectStructure(project, paths);
			IFolder destFolder = project.getFolder("/model");
			copyFile(new File(location), destFolder);
		} catch (CoreException e) {
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	protected static org.eclipse.uml2.uml.Package readUMLModel(URI uri) {
		org.eclipse.uml2.uml.Package package_ = null;
		try {
			// Load the requested resource
			Resource resource = new ResourceSetImpl()
					.getResource(org.eclipse.emf.common.util.URI.createURI(uri.toString()), true);

			// Get the first (should be only) package from it
			package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),
					UMLPackage.Literals.PACKAGE);
			System.out.println(package_.getName());
		} catch (WrappedException we) {
			System.exit(1);
		}

		return package_;
	}

	/**
	 * Just do the basics: create a basic project.
	 *
	 * @param location
	 * @param projectName
	 */
	private static IProject createBaseProject(String projectName, URI location) {
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);
			try {
				newProject.create(desc, null);
				if (!newProject.isOpen()) {
					newProject.open(null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}

	/**
	 * Create a folder structure with a parent root, overlay, and a few child
	 * folders.
	 *
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */
	private static void addToProjectStructure(IProject newProject, String[] paths) throws CoreException {
		 for (String path : paths) {
	            IFolder etcFolders = newProject.getFolder(path);
	            createFolder(etcFolders);
	       }
	}	
	
	private static void createFolder(IFolder folder) throws CoreException {
        IContainer parent = folder.getParent();
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent);
        }
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
    }
	
	private static void copyFile(File srcFile, IFolder destFolder) {
		IFile newFile = destFolder.getFile(new Path(srcFile.getName()));
		try {
			newFile.create(new FileInputStream(srcFile), true, null);
		} catch (FileNotFoundException | CoreException e) {
			e.printStackTrace();
		}
	}

	private static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(SonarQubeProjectNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = SonarQubeProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);

			IProgressMonitor monitor = null;
			project.setDescription(description, monitor);
		}
	}

}
