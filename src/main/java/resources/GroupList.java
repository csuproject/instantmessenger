package resources;

import java.util.ArrayList;
import java.util.LinkedList;

public class GroupList implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LinkedList<String> groupList;
	
	public LinkedList<String> getList() {
		return groupList;
	}
	
	public void setList(LinkedList<String> groupList) {
		this.groupList = groupList;
	}

}
