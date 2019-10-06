package archtoring.handlers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;

import archtoring.utils.EolStandalone;
import archtoring.utils.EplStandalone;
import archtoring.utils.EpsilonStandalone;

public class EPLHandler {

	private static final String[] FILES = { "epl/controllers.epl", "epl/models.epl", "epl/repositories.epl", "epl/services.epl", "epl/tos.epl" };
	public void execute() {
		for (String eplFile : FILES) {
			EplStandalone eol = new EplStandalone();
			eol.setSource(eplFile);
			List<IModel> models = new ArrayList<IModel>();
			try {
				System.out.println("Running pattern on back.");
				models.add(EpsilonStandalone.createEmfModelByURI(
						"javaModel", "file://" + System.getProperty("user.dir").replace("\\", "/") + "/" + "cpe-seguridad-api"
								+ "/cpe-seguridad-api_java.xmi",
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
		
		EolStandalone eol = new EolStandalone();
		eol.setSource("epl/categorization.eol");
		List<IModel> models = new ArrayList<IModel>();
		try {
			System.out.println("Getting categorization.");
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
