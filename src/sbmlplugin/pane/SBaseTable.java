package sbmlplugin.pane;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.sbml.libsbml.ListOf;
import org.sbml.libsbml.SBase;

// TODO: Auto-generated Javadoc
/**
 * Spatial SBML Plugin for ImageJ.
 *
 * @author Kaito Ii <ii@fun.bio.keio.ac.jp>
 * @author Akira Funahashi <funa@bio.keio.ac.jp>
 * Date Created: Jan 13, 2016
 */
@SuppressWarnings("serial")
public abstract class SBaseTable {
	
	/** The pane. */
	protected JScrollPane pane;
	
	/** The panel. */
	protected JPanel panel;
	
	/** The gray. */
	protected Color gray = new Color(169, 169, 169);
	
	/** The member list. */
	protected ArrayList<SBase> memberList = new ArrayList<SBase>();
	
	/** The list. */
	protected ListOf list;
	
	/**
	 * Adds the.
	 */
	abstract void add();

	/**
	 * Edits the.
	 *
	 * @param index the index
	 */
	abstract void edit(int index);
	
	/**
	 * Removes the from list.
	 *
	 * @param index the index
	 */
	void removeFromList(int index){
		if(index == -1) return;
		String id = memberList.get(index).getId();
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).getId().equals(id)) 
				list.remove(i);
		memberList.remove(index);
	}
	
	/**
	 * Removes the selected from table.
	 *
	 * @param table the table
	 */
	void removeSelectedFromTable(JTable table) {
		int row = table.getSelectedRow();
		if(row == -1) return;
		((DefaultTableModel) table.getModel()).removeRow(row);
	}
	
	/**
	 * Gets the pane.
	 *
	 * @return the pane
	 */
	JScrollPane getPane(){
		return pane;
	}
	
	/**
	 * Contains duplicate id.
	 *
	 * @param sbase the sbase
	 * @return true, if successful
	 */
	boolean containsDuplicateId(SBase sbase){
		Boolean bool = false;
		
		for(SBase s: memberList)
			if(s != sbase && s.getId().equals(sbase.getId())) bool = true;

		return bool;
	}

	/**
	 * Err dup ID.
	 *
	 * @param table the table
	 */
	void errDupID(JTable table){
		JOptionPane.showMessageDialog(table, "Duplicate id", "Error", JOptionPane.PLAIN_MESSAGE);	
	}
	
	/**
	 * Sets the table properties.
	 *
	 * @param table the new table properties
	 */
	void setTableProperties(JTable table){
		setColumnSize(table);
		table.setBackground(gray);
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Sets the column size.
	 *
	 * @param table the new column size
	 */
	void setColumnSize(JTable table) {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Enumeration<TableColumn> e = table.getColumnModel().getColumns();
		while (e.hasMoreElements())
			e.nextElement().setMinWidth(150);
	}
	
	/**
	 * Sets the table to scroll.
	 *
	 * @param name the name
	 * @param table the table
	 * @return the j scroll pane
	 */
	JScrollPane setTableToScroll(String name, JTable table){
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setName(name);
		return scroll;
	}
	
	/**
	 * The Class MyTableModel.
	 */
	protected class MyTableModel extends DefaultTableModel {
		
		/**
		 * Instantiates a new my table model.
		 *
		 * @param data the data
		 * @param header the header
		 */
		public MyTableModel(Object[][] data, String[] header) {
			super(data, header);
		}

		/**
		 * Instantiates a new my table model.
		 *
		 * @param i the i
		 * @param j the j
		 */
		public MyTableModel(int i, int j) {
			super(i,j);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
        /**
         * Update row.
         *
         * @param index the index
         * @param vector the vector
         */
        public void updateRow(int index,Vector<Object> vector)
        {
            for (int i = 0 ; i < vector.size() ; i++)
            {
                setValueAt(vector.get(i), index, i);
            }
        }
	}
}
