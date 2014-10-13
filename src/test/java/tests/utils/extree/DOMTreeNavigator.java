package tests.utils.extree;

import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import utils.extree.TreeNavigator;
import utils.extree.TreeNavigator.NavigationMode;
import utils.extree.TreeNode;

/* TODO: notare che è praticamente una implementazione di MaskBuilder (progetto PlantAdapter):
 * occorre però valutare il caso di un dispositivo con due diversi padri che poi si "riuniscano"
 * in un unica radice (a quel punto il comando da inviare è uno solo ma NON SI HA PIU' UN ALBERO,
 * occorre valutare se e come la cosa è rappresentabile con XML)
 * 
 * Probabilmente "ExtensibileTree" dovrà gestire almeno grafi diretti (in realtà in parte già lo fa...)
 */

public class DOMTreeNavigator implements TreeNavigator<Node> {

	private Document xml;
	private Stack<Node> nodeStack;
	private NavigationMode mode;
	
	public DOMTreeNavigator(NavigationMode mode) throws ParserConfigurationException {
		this.xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		this.nodeStack = new Stack<Node>();
		this.nodeStack.push(this.xml);
		this.mode = mode;
	}
	
	public Document getXML() {
		return this.xml;
	}

	@Override
	public void preorderVisit(TreeNode<Node> node) {
		Node importedNode =  this.xml.importNode(node.getElement(), false);
		this.nodeStack.peek().appendChild(importedNode);
		this.nodeStack.push(importedNode);
	}

	@Override
	public void postorderVisit(TreeNode<Node> node) {
		this.nodeStack.pop();
	}

	@Override
	public NavigationMode getChildrenNavigationMode() {
		return mode;
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