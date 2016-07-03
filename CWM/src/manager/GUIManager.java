package manager;

import java.awt.EventQueue;
import javax.swing.JFrame;

import main.Main;

public class GUIManager extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIManager frame = new GUIManager();
					frame.setVisible(true);
				} catch (Throwable e) {
					Main.handleError(e,Thread.currentThread(),"ERROR:");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIManager() {
		getContentPane().setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
	}

}
