package archtoring.wizard;

import java.io.File;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class UMLWizardPage extends WizardPage implements Listener {
	
    private Text projectName;
    private Composite container;
	private Button selectFileButton;

	private java.net.URI locationURI;

	private Text projectURI;

    public UMLWizardPage() {
        super("Import UML model");
        setTitle("Import UML component diagram of architecture");
        setDescription("Select UML file to import");
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Project name");

        projectName = new Text(container, SWT.BORDER | SWT.SINGLE);
        projectName.setText("");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectName.setLayoutData(gd);
        projectName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!projectName.getText().isEmpty()) {
					setErrorMessage(null);
					setPageComplete(true);
				}
				else
					setErrorMessage("You must input a name for the project!");
			}
		});
        
        projectURI = new Text(container, SWT.BORDER | SWT.SINGLE);
        projectURI.setText("");
        projectURI.setLayoutData(gd);

        selectFileButton = new Button(container, SWT.PUSH);
        selectFileButton.setText("Browse");
        selectFileButton.addListener(SWT.Selection, this);
        
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);

    }

    public String getProjectName() {
        return projectName.getText();
    }
    
    public java.net.URI getLocationURI() {
    	return locationURI;
    }

	@Override
	public void handleEvent(Event event) {
		if(event.widget == selectFileButton) {
			DirectoryDialog dialog = new DirectoryDialog(UMLWizardPage.this.container.getShell(), SWT.OPEN);
			dialog.setText("Select Directory to Create Project");
			if (dialog.open() != null) {
				locationURI = new File(dialog.getFilterPath()).toURI();
				projectURI.setText(locationURI.getPath());
				setPageComplete(true);
			}
		}
	}
}