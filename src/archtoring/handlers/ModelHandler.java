package archtoring.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.emf.ecore.xmi.XMLResource;

public class ModelHandler {

	private static final int FLUSH_LIMIT_SHIFT = 16;
	private static final Integer FLUSH_LIMIT = Integer.valueOf(1 << FLUSH_LIMIT_SHIFT);
	public static String backName;
	public static String frontName;
	public static List<String> args;
	public static String projectName;
	public static String key;
	
	public ModelHandler(List<String> args) {
		ModelHandler.args = args;
	}
	
	public static List<String> getArgs(){
		return args;
	}

	public static String[] getNames() {
		String[] names = { backName, frontName };
		return names;
	}

	public void execute() {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse("./pom.xml");
			projectName = document.getElementsByTagName("artifactId").item(0).getTextContent();
			String groupId = document.getElementsByTagName("groupId").item(0).getTextContent();
			
			key = groupId + ":" + projectName;
			
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();

			frontName = projectName + "-api";
			backName = projectName + "-back";

			IProjectDescription projectDescriptionBack = workspace.newProjectDescription(backName);
			IProjectDescription projectDescriptionFront = workspace.newProjectDescription(frontName);

			projectDescriptionBack.setLocation(new Path(System.getProperty("user.dir") + "/" + backName));
			projectDescriptionFront.setLocation(new Path(System.getProperty("user.dir") + "/" + frontName));

			IProject projectBack = workspace.getRoot().getProject(projectDescriptionBack.getName());
			IProject projectFront = workspace.getRoot().getProject(projectDescriptionFront.getName());

			System.out.println("Importing projects");
			try {
				projectBack.create(projectDescriptionBack, null);
				projectFront.create(projectDescriptionFront, null);
			} catch (CoreException e) {
				projectBack.delete(false, true, null);
				projectFront.delete(false, true, null);
				projectBack.create(projectDescriptionBack, null);
				projectFront.create(projectDescriptionFront, null);
			}
			projectBack.open(null);
			projectFront.open(null);

			IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
			ResolverConfiguration configuration = new ResolverConfiguration();
			configurationManager.enableMavenNature(projectBack, configuration, new NullProgressMonitor());
			configurationManager.enableMavenNature(projectFront, configuration, new NullProgressMonitor());
			configurationManager.updateProjectConfiguration(projectBack, new NullProgressMonitor());
			configurationManager.updateProjectConfiguration(projectFront, new NullProgressMonitor());

			IJavaProject backProject = JavaCore.create(projectBack);
			IJavaProject frontProject = JavaCore.create(projectFront);

			System.out.println("Discovering projects");

			Map<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_FLUSH_THRESHOLD, FLUSH_LIMIT);
			options.put(XMLResource.OPTION_USE_FILE_BUFFER, Boolean.TRUE);

			DiscoverJavaModelFromJavaProject discovererBack = new DiscoverJavaModelFromJavaProject();
			discovererBack.setSerializeTarget(true);
			discovererBack.discoverElement(backProject, new NullProgressMonitor());
			Resource resourceBack = discovererBack.getTargetModel();
			resourceBack.save(options);

			DiscoverJavaModelFromJavaProject discovererFront = new DiscoverJavaModelFromJavaProject();
			discovererFront.setSerializeTarget(true);
			discovererFront.discoverElement(frontProject, new NullProgressMonitor());
			Resource resourceFront = discovererFront.getTargetModel();
			resourceFront.save(options);

		} catch (DiscoveryException e) {
			System.out.println("Discovery error.");
			e.printStackTrace();
		} catch (CoreException e1) {
			System.out.println("Project handling error.");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("Editing error.");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.out.println("POM parsing error.");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("POM parsing error.");
			e.printStackTrace();
		}
	}
}
