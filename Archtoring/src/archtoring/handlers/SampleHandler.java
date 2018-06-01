package archtoring.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Test2",
				"Hello, Eclipse world");
		GenerateJavaExtended javaGenerator;
		try {
			javaGenerator = new GenerateJavaExtended(URI.createFileURI("C:/Users/Asistente/Desktop/Epsilon/Test2/Test_java.xmi"),
				    new File("C:/Users/Asistente/Desktop/PruebaModelo"), new ArrayList<Object>());

			javaGenerator.doGenerate(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
