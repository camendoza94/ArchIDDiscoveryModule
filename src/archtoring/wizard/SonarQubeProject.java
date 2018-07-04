package archtoring.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import archtoring.wizard.SonarQubeProjectNature;;
 
public class SonarQubeProject {
    /**
     * For this marvelous project we need to:
     * - create the default Eclipse project
     * - add the custom project nature
     * - create the folder structure
     *
     * @param projectName
     * @param location
     * @param natureId
     * @return
     */
    public static IProject createProject(String projectName) {
        Assert.isNotNull(projectName);
        Assert.isTrue(projectName.trim().length()> 0);
 
        IProject project = createBaseProject(projectName, null);
        try {
            addNature(project);
            addToProjectStructure(project);
        } catch (CoreException e) {
            e.printStackTrace();
            project = null;
        }
 
        return project;
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
