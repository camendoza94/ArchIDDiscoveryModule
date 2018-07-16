package archtoring.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.emf.ecore.xmi.XMLResource;


public class ModelHandler extends AbstractHandler {

	private static final int FLUSH_LIMIT_SHIFT = 16;
	private static final Integer FLUSH_LIMIT = Integer.valueOf(1 << FLUSH_LIMIT_SHIFT);

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
					DiscoverJavaModelFromJavaProject discoverer = new DiscoverJavaModelFromJavaProject();
					try {
						discoverer.setSerializeTarget(true);
						discoverer.discoverElement(project, new NullProgressMonitor());
						Resource resource = discoverer.getTargetModel();
						Map<String, Object> options = new HashMap<String, Object>();
						options.put(XMLResource.OPTION_FLUSH_THRESHOLD, FLUSH_LIMIT);
						options.put(XMLResource.OPTION_USE_FILE_BUFFER, Boolean.TRUE);
						try {
							resource.save(options);
						} catch (IOException e) {
							System.out.println("Editing error.");
							e.printStackTrace();
						}
					} catch (DiscoveryException e) {
						System.out.println("Discovery error.");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
