package archtoring;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import archtoring.handlers.EPLHandler;
import archtoring.handlers.ModelHandler;

public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		ModelHandler model = new ModelHandler();
		model.execute();
		EPLHandler epl = new EPLHandler();
		epl.execute(model.getNames());
		return EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
