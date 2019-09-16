package archtoring.handlers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import archtoring.utils.EplStandalone;
import archtoring.utils.EpsilonStandalone;

public class EPLHandler {

	private static final String[] FILES_BACK = { "epl/logic.epl", "epl/dependencies.epl" };
	private static final String[] FILES_FRONT = { "epl/dtos.epl", "epl/detailDTOs.epl", "epl/resources.epl", "epl/dependencies.epl" };

	public void execute(String[] names) {
		for (String eplFile : FILES_BACK) {
			EplStandalone eol = new EplStandalone();
			eol.setSource(eplFile);
			List<IModel> models = new ArrayList<IModel>();
			try {
				System.out.println("Running pattern on back.");
				models.add(EpsilonStandalone.createEmfModelByURI(
						"javaModel", "file://" + System.getProperty("user.dir").replace("\\", "/") + "/" + names[0]
								+ "/" + names[0] + "_java.xmi",
						EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
				models.add(EpsilonStandalone.createEmfModel("rulesModel", "epl/ReferenceArchitecture.xmi",
						"epl/rules.ecore", true, true));
				eol.setModels(models);
				eol.execute(true);
			} catch (EolModelLoadingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (String eplFile : FILES_FRONT) {
			EplStandalone eol = new EplStandalone();
			eol.setSource(eplFile);
			List<IModel> models = new ArrayList<IModel>();
			try {
				System.out.println("Running pattern on front.");
				models.add(EpsilonStandalone.createEmfModelByURI(
						"javaModel", "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/" + names[1]
								+ "/" + names[1] + "_java.xmi",
						EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
				models.add(EpsilonStandalone.createEmfModel("rulesModel", "epl/ReferenceArchitecture.xmi",
						"epl/rules.ecore", true, true));
				eol.setModels(models);
				eol.execute(true);
			} catch (EolModelLoadingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
