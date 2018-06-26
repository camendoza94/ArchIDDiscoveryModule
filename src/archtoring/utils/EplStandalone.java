package archtoring.utils;

import java.util.List;

import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.epl.EplModule;

public class EplStandalone extends EpsilonStandalone {

	@Override
	public IEolExecutableModule createModule() {
		return new EplModule();
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
