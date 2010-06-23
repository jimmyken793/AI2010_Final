package idv.jimmyken793.aiproj;
import idv.jimmyken793.aiproj.chinese.PhoneticSymbolSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainWindow {

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
	private Text textArea = null;
	private String log = "";  //  @jve:decl-index=0:
	private String keylog = ""; 
	
	private PhoneticSymbolSet p = new PhoneticSymbolSet();
	public static void main(String[] args){
		org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();
		MainWindow window=new MainWindow();
		/*
		 * if (args.length > 0) { for (int i = 0; i < args.length; i++) { String
		 * acc = args[i]; AlbumAccount waa = new AlbumAccount(acc);
		 * FileIO.ensureDirectory("WretchRipper"); ArrayList<Album> albums =
		 * waa.getAlbums(); Iterator<Album> it = albums.iterator(); while
		 * (it.hasNext()) { Album a = it.next(); // System.out.println(a.getId()
		 * + "\t" + a.getTitle() + "\t" // + a.getPictures().size() +
		 * " pictures"); mainWindow.getCombo().add(a.getTitle());
		 * mainWindow.getAlbumList().add(a.getTitle()); } // waa.download(); } }
		 */
		while (!window.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	/**
	 * This method initializes sShell
	 */
	public MainWindow(){
		createSShell();
	}
	public boolean isDisposed(){
		return sShell.isDisposed();
	}
	private void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(588, 392));
		textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textArea.setLayoutData(gridData);
		textArea.setEditable(false);
		textArea.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.BS) {
					p.delete();
				} else {
					p.push(e.character);
				}
				if (p.isDone()) {
					if (log.length() > 0)
						log += ",";
					log += p;
					keylog+=p.getKeys();
					p.clear();
				}
				textArea.setText(p.toString() + "\n" + log);
			}
		});
		sShell.open();
	}

}
