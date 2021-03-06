/*
 * LineNumber.java
 *
 * Created on 27. April 2006, 19:30
 */

package jasmin.gui;

import java.awt.Font;
import java.awt.event.MouseEvent;

/**
 * @author Kai Orend
 */
public class LineNumber extends javax.swing.JPanel {
	
	private static final long serialVersionUID = 1L;
	int line;
	private JasDocument doc = null;
	
	/** Creates new form LineNumber */
	public LineNumber(int line, JasDocument doc) {
		initComponents();
		this.line = line;
		this.doc = doc;
		jLabel1.setFont(new java.awt.Font(doc.frame.getProperty("font"), Font.PLAIN, doc.frame.getProperty("font.size", 12)));
		jLabel1.setText("" + line);
		doLayout();
	}
	
	public boolean isBreakPoint() {
		return jToggleButton1.isSelected();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jLabel1 = new javax.swing.JLabel();
		jToggleButton1 = new javax.swing.JToggleButton();
		
		setLayout(new java.awt.BorderLayout());
		
		addMouseListener(new java.awt.event.MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}
		});
		
		jLabel1.setText("jLabel1");
		jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jLabel1MouseClicked(evt);
			}
		});
		
		add(jLabel1, java.awt.BorderLayout.EAST);
		
		jToggleButton1.setBorderPainted(false);
		jToggleButton1.setContentAreaFilled(false);
		jToggleButton1.setMaximumSize(new java.awt.Dimension(10, 10));
		jToggleButton1.setMinimumSize(new java.awt.Dimension(10, 10));
		jToggleButton1.setPreferredSize(new java.awt.Dimension(10, 10));
		jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
			
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jToggleButton1ActionPerformed(evt);
			}
		});
		
		add(jToggleButton1, java.awt.BorderLayout.WEST);
		
	}// </editor-fold>//GEN-END:initComponents
	
	private void formMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseClicked
		jLabel1MouseClicked(evt);
	}// GEN-LAST:event_formMouseClicked
	
	private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel1MouseClicked
		if (evt.getButton() == MouseEvent.BUTTON1) {
			jToggleButton1.setSelected(!jToggleButton1.isSelected());
			jToggleButton1ActionPerformed(null);
		} else if (evt.getButton() == MouseEvent.BUTTON3) {
			doc.data.setInstructionPointer(line);
			doc.updateAll();
		}
	}// GEN-LAST:event_jLabel1MouseClicked
	
	/**
	 * @param evt
	 *        the Event that triggered this action
	 */
	private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton1ActionPerformed
	
		if (jToggleButton1.isSelected()) {
			jToggleButton1
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/breakpoint.png")));
		} else {
			jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/leer.gif")));
		}
		
	}// GEN-LAST:event_jToggleButton1ActionPerformed
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JToggleButton jToggleButton1;
	// End of variables declaration//GEN-END:variables
	
}
