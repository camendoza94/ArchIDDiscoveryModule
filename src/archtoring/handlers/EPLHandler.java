package archtoring.handlers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.handlers.HandlerUtil;

import archtoring.utils.EplStandalone;
import archtoring.utils.EpsilonStandalone;

import org.eclipse.jface.viewers.IStructuredSelection;

public class EPLHandler extends AbstractHandler {

	private static final String[] FILES = { "epl/dtos.epl", "epl/detailDtos.epl",
			"epl/logic.epl", "epl/resources.epl" };

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
					try {
						for (String eplFile : FILES) {
							EplStandalone eol = new EplStandalone();
							eol.setSource(eplFile);
							List<IModel> models = new ArrayList<IModel>();
							models.add(EpsilonStandalone.createEmfModelByURI("Model",project.getResource().getFullPath() + "/" + project.getElementName() + "_java.xmi",
									EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
							eol.setModels(models);
							eol.execute(true);
						}
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
		}
		return null;
	}
}
