package archtoring.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromProject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class ModelHandler {

	private static final int FLUSH_LIMIT_SHIFT = 16;
	private static final Integer FLUSH_LIMIT = Integer.valueOf(1 << FLUSH_LIMIT_SHIFT);

	public void execute() {

		try {

			final IWorkspace workspace = ResourcesPlugin.getWorkspace();

			IProjectDescription projectDescription = workspace.newProjectDescription("cpe-seguridad-api");

			projectDescription.setLocation(new Path(System.getProperty("user.dir") + "/" + "cpe-seguridad-api"));

			String[] newNatures = new String[3];
			newNatures[0] = "org.eclipse.jdt.core.javanature";
			newNatures[1] = "org.eclipse.wst.common.project.facet.core.nature";
			newNatures[2] = "org.eclipse.buildship.core.gradleprojectnature";
			projectDescription.setNatureIds(newNatures);

			ICommand[] buildSpec = projectDescription.getBuildSpec();
			ICommand command1 = projectDescription.newCommand();
			command1.setBuilderName("org.eclipse.jdt.core.javabuilder");
			ICommand command2 = projectDescription.newCommand();
			command2.setBuilderName("org.eclipse.wst.common.project.facet.core.builder");
			ICommand command3 = projectDescription.newCommand();
			command3.setBuilderName("org.eclipse.wst.validation.validationbuilder");
			ICommand command4 = projectDescription.newCommand();
			command4.setBuilderName("org.eclipse.buildship.core.gradleprojectbuilder");
			Collection<ICommand> list = new ArrayList<>(Arrays.asList(buildSpec));
			list.add(command1);
			list.add(command2);
			list.add(command3);
			list.add(command4);
			
			projectDescription.setBuildSpec(list.toArray(new ICommand[list.size()]));

			IProject project = workspace.getRoot().getProject(projectDescription.getName());

			System.out.println("Importing projects");
			try {
				project.create(projectDescription, null);
			} catch (CoreException e) {
				project.delete(false, true, null);
				project.create(projectDescription, null);
			}

			project.open(null);
			
			//IJavaProject iProject = JavaCore.create(project);

			System.out.println("Discovering projects");

			Map<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_FLUSH_THRESHOLD, FLUSH_LIMIT);
			options.put(XMLResource.OPTION_USE_FILE_BUFFER, Boolean.TRUE);
			options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

			DiscoverJavaModelFromProject discoverer = new DiscoverJavaModelFromProject();
			discoverer.setSerializeTarget(true);
			discoverer.discoverElement(project, new NullProgressMonitor());
			Resource resource = discoverer.getTargetModel();
			resource.save(options);

		} catch (DiscoveryException e) {
			System.out.println("Discovery error.");
			e.printStackTrace();
		} catch (CoreException e1) {
			System.out.println("Project handling error.");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("Editing error.");
			e.printStackTrace();
		}
	}
}
