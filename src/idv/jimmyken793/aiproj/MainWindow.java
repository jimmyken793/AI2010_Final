package idv.jimmyken793.aiproj;

import java.io.IOException;
import java.sql.SQLException;

import idv.jimmyken793.aiproj.chinese.PhoneticSymbolSet;
import idv.jimmyken793.aiproj.data.StringData;
import idv.jimmyken793.aiproj.database.Database;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainWindow {

	private Shell sShell = null;
	private Text textArea = null;
	private String log = "";
	private String keylog = "";
	private String output = ""; // @jve:decl-index=0:
	private String pre1 = ""; // @jve:decl-index=0:
	private String pre2 = ""; // @jve:decl-index=0:
	private String errormes = ""; // @jve:decl-index=0:

	private PhoneticSymbolSet p = new PhoneticSymbolSet();

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();
		MainWindow window = new MainWindow();
		while (!window.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public MainWindow() throws ClassNotFoundException, SQLException {
		db = new Database();
		createSShell();
		sShell.open();
	}

	public boolean isDisposed() {
		return sShell.isDisposed();
	}

	private Database db;
	private String scentence = ""; // @jve:decl-index=0:

	private void createSShell(){
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
				errormes = "";
				if (e.keyCode == SWT.BS) {
					p.delete();
				} else if (e.keyCode == SWT.CR) {
					if (pre2.length() > 0) {
						output += " / ";
						pre1 = pre2 = "";
						System.out.println(scentence);
						try {
							DataScanner.parseFile(new StringData(scentence), db);
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						scentence = "";
					}
				} else {
					p.push(e.character);
					if (p.isDone()) {
						try {
							String s = Decider.decide(db, pre1, pre2, p.getKeys());
							if (log.length() > 0)
								log += ",";
							log += p;
							keylog += p.getKeys();
							output = output.substring(0, output.length() - (s.length() - 1)) + s;
							scentence = scentence.substring(0, scentence.length() - (s.length() - 1)) + s;
							pre1 = pre2;
							pre2 = p.getKeys();
						} catch (Exception ee) {
							errormes = "Error：無此讀音。";
						}

						p.clear();
					}
				}
				textArea.setText(p.toString() + "\n" + log + "\n" + output + "\n\n" + errormes);
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="178,72"
