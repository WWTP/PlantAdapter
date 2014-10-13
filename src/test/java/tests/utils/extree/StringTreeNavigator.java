package tests.utils.extree;

import java.util.List;

import utils.extree.TreeNavigator;
import utils.extree.TreeNavigator.NavigationMode;
import utils.extree.TreeNode;

public class StringTreeNavigator implements TreeNavigator<String> {

	private List<String> log;
	
	public StringTreeNavigator(List<String> log) {
		this.log = log;
	}
	
	public String[] getLog() {
		return this.log.toArray(new String[0]);
	}
	
	public void printLog() {
		for (String s : this.log) System.out.println(s);
	}
	
	@Override
	public void preorderVisit(TreeNode<String> node) {
		this.log.add(node.getElement());
	}

	@Override
	public void postorderVisit(TreeNode<String> node) {
		/* do nothing */
	}

	@Override
	public TreeNavigator.NavigationMode getChildrenNavigationMode() {
		return NavigationMode.LEFTMOST;
	}

	@Override
	public boolean goOn() {
		return true;
	}

	@Override
	public boolean skipChilden() {
		return false;
	}
}