package archtoring.utils;

import java.util.List;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.models.IModel;

/**
 * This example demonstrates using the Epsilon Object Language, the core
 * language of Epsilon, in a stand-alone manner
 * 
 * @author Dimitrios Kolovos
 */
public class EolStandalone extends EpsilonStandalone {

	@Override
	public IEolExecutableModule createModule() {
		return new EolModule();
	}

	@Override
	public void postProcess() {

	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public void setModels(List<IModel> models) {
		this.models = models;		
	}

}
