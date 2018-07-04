package archtoring.utils;

import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolPrimitiveType;

public class EglStandalone extends EpsilonStandalone {

	@Override
	public IEolExecutableModule createModule() {
		return new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory());
	}
		
	public void setLocation(String Uri) {
		parameters.add(new Variable("projectUri", Uri, EolPrimitiveType.String));
	}
}
