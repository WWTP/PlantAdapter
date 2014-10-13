package tests.utils.extree;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.extree.ExtensibleTree;
import utils.extree.TreeNavigator.NavigationMode;

public class TestMain {

	private static void printDOMElements(Node root, int step) {
		String ident = "";
		for (int i = 0; i < step; i++) ident += " ";
		System.out.println(ident + root.getNodeName());
		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			printDOMElements(root.getChildNodes().item(i), step + 1);
		}
	}
	
	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws ParserConfigurationException {
		// Creazione DOM
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element aggr1 = xml.createElement("DT80");
		Element aggr2 = xml.createElement("ADAM");
		Element cmd1 = xml.createElement("CMD1");
		Element cmd2 = xml.createElement("CMD2");
		Element cmd3 = xml.createElement("CMD3");
		Element cmd4 = xml.createElement("CMD4");
		aggr1.appendChild(cmd1);
		aggr1.appendChild(aggr2);
		aggr2.appendChild(cmd2);
		aggr2.appendChild(cmd3);
		aggr1.appendChild(cmd4);
		xml.appendChild(aggr1);
		// Navigazione DOM
		System.out.println("Output generato da navigazione DOM:\n");
		printDOMElements(xml.getFirstChild(), 0);
		// Creazione ET
		ExtensibleTree<Node> tree = new ExtensibleTree<Node>(); 
		tree.addElement(cmd1);
		tree.addElement(cmd2);
		tree.addElement(cmd3);
		tree.addElement(cmd4);
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(cmd2);
		nodes.add(cmd3);
		tree.extend(aggr2, nodes);
		nodes = new ArrayList<Node>();
		nodes.add(cmd1);
		nodes.add(aggr2);
		nodes.add(cmd4);
		tree.extend(aggr1, nodes);
		// Navigazione ET
		System.out.println("\nOutput generato da navigazione ET (LEFTMOST):\n");
		DOMTreeNavigator navigator = new DOMTreeNavigator(NavigationMode.LEFTMOST);
		tree.navigate(navigator);
		printDOMElements(navigator.getXML().getFirstChild(), 0);
		System.out.println("\nOutput generato da navigazione ET (RIGHTMOST):\n");
		navigator = new DOMTreeNavigator(NavigationMode.RIGHTMOST);
		tree.navigate(navigator);
		printDOMElements(navigator.getXML().getFirstChild(), 0);
	}
}