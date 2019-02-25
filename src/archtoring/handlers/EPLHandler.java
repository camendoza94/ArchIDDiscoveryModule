package archtoring.handlers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import archtoring.utils.EplStandalone;
import archtoring.utils.EpsilonStandalone;

public class EPLHandler {

	private static final String[] FILES = { "detailDTOs.epl" };

	public void execute(String[] names) {
		for (String eplFile : FILES) {
			EplStandalone eol = new EplStandalone();
			//TODO Change EPLs and models reference
			eol.setSource("C:/Users/Asistente/Documents/epl/" + eplFile);
			List<IModel> models = new ArrayList<IModel>();
			try {
				System.out.println("Running pattern on back.");
				models.add(EpsilonStandalone.createEmfModelByURI("javaModel",
						System.getProperty("user.dir") + "/" + names[0] + "/" + names[0] + "_java.xmi",
						EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
				models.add(EpsilonStandalone.createEmfModel("rulesModel",
						"C:/Users/Asistente/Documents/models/ReferenceArchitecture.xmi",
						"C:/Users/Asistente/Documents/models/rules.ecore", true, true));
				eol.setModels(models);
				eol.execute();

				System.out.println("Running pattern on front.");
				models.clear();
				models.add(EpsilonStandalone.createEmfModelByURI("javaModel",
						System.getProperty("user.dir") + "/" + names[1] + "/" + names[1] + "_java.xmi",
						EpsilonStandalone.MODISCO_JAVA_METAMODEL_URI, true, true));
				models.add(EpsilonStandalone.createEmfModel("rulesModel",
						"C:/Users/Asistente/Documents/models/ReferenceArchitecture.xmi",
						"C:/Users/Asistente/Documents/models/rules.ecore", true, true));
				eol.setModels(models);
				eol.execute();
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
