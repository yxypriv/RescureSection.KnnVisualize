package visualization.actionListeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import utils.ConfigurationUtil;
import visualization.interfaces.ActionCallback;

/**
 * generic load file action
 * 
 * @author Xingyu
 */
public class LoadFileActionListener implements ActionListener {

	String propertyKey = null;
	Component parent = null;
	ActionCallback callback = null;

	/**
	 * @param propertyKey
	 *            will save location if exist. <b>null</b> means will not save.
	 * @param parent
	 *            the component who called this action
	 * @param callback
	 *            will put in the file path, you can finish your action here.
	 */
	public LoadFileActionListener(String propertyKey, Component parent, ActionCallback callback) {
		super();
		this.propertyKey = propertyKey;
		this.parent = parent;
		this.callback = callback;
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String previousFilePath = ConfigurationUtil.get(propertyKey);
		if (null != previousFilePath) {
			chooser.setCurrentDirectory(new File(previousFilePath));
		}
		int chooseReturn = chooser.showOpenDialog(parent);
		if (chooseReturn == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (propertyKey != null)
				ConfigurationUtil.update(propertyKey, selectedFile.getAbsolutePath());
			callback.process(selectedFile.getAbsolutePath());
		} else {
			System.out.println(String.format("Selection canceled~"));
			callback.process(null);
		}
	}

}
