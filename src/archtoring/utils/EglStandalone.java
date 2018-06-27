package archtoring.utils;

import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.IEolExecutableModule;

public class EglStandalone extends EpsilonStandalone {

	@Override
	public IEolExecutableModule createModule() {
		return new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory());
	}
}
