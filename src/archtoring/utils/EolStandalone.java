package archtoring.utils;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;

/**
 * This example demonstrates using the Epsilon Object Language, the core
 * language of Epsilon, in a stand-alone manner
 * 
 * @author Dimitrios Kolovos
 */
public class EolStandalone extends EpsilonStandalone {

	@Override
	public IEolModule createModule() {
		return new EolModule();
	}
}
