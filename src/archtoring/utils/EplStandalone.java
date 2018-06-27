package archtoring.utils;

import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.epl.EplModule;

public class EplStandalone extends EpsilonStandalone {

	@Override
	public IEolExecutableModule createModule() {
		return new EplModule();
	}
}
