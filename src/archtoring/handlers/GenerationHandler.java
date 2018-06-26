package archtoring.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.ui.handlers.HandlerUtil;

import archtoring.utils.EolStandalone;
import archtoring.utils.EpsilonStandalone;

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
							EolStandalone eol = new EolStandalone();
							eol.setSource("epl/comments.eol");
							List<IModel> models = new ArrayList<IModel>();
							models.add(EpsilonStandalone.createEmfModelByURI("Model", file.getFullPath().toString(),
									EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
							eol.setModels(models);
							eol.execute(true);
							javaGenerator = new GenerateJavaExtended(URI.createFileURI(file.getFullPath().toString()),
									new File(dialog.getFilterPath()), new ArrayList<Object>());
							javaGenerator.doGenerate(null);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
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
