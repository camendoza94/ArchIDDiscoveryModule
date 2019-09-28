package archtoring;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import archtoring.handlers.EPLHandler;
import archtoring.handlers.DataHandler;
import archtoring.handlers.ModelHandler;

public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		ModelHandler model = new ModelHandler();
		model.execute();
		DataHandler data = new DataHandler();
		EPLHandler epl = new EPLHandler();
		epl.execute();
		data.execute();
		return EXIT_OK;
	}

	@Override
	public void stop() {
	}

}
