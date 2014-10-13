package tests.utils.extree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.extree.ExtensibleTree;
import utils.extree.NavigableTree;
import utils.extree.TreeNavigator.NavigationMode;;

public class ExtensibleTreeTest {

	private List<String> log1;
	private List<String> log2;
	private Document xml;
	
	@Before
	public void before() {
		this.log1 = new ArrayList<String>();
		this.log2 = new ArrayList<String>();
		// Creazione DOM
		try {
			this.xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} 
		catch (ParserConfigurationException e) {
			fail("Parse error");
		}
		Element aggr1 = this.xml.createElement("DT80");
		Element aggr2 = this.xml.createElement("ADAM");
		Element cmd1 = this.xml.createElement("CMD1");
		Element cmd2 = this.xml.createElement("CMD2");
		Element cmd3 = this.xml.createElement("CMD3");
		Element cmd4 = this.xml.createElement("CMD4");
		aggr1.appendChild(cmd1);
		aggr1.appendChild(aggr2);
		aggr2.appendChild(cmd2);
		aggr2.appendChild(cmd3);
		aggr1.appendChild(cmd4);
		this.xml.appendChild(aggr1);
		logDOMElements(this.xml, this.log1);
	}
	
	private static void logDOMElements(Node root, List<String> log) {
		log.add(root.getNodeName());
		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			logDOMElements(root.getChildNodes().item(i), log);
		}
	}

	@Test
	public final void testExtendNavigate() throws ParserConfigurationException {
		ExtensibleTree<Node> tree = new ExtensibleTree<Node>(); 
		tree.addElement(this.xml.getElementsByTagName("CMD1").item(0));
		tree.addElement(this.xml.getElementsByTagName("CMD2").item(0));
		tree.addElement(this.xml.getElementsByTagName("CMD3").item(0));
		tree.addElement(this.xml.getElementsByTagName("CMD4").item(0));
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(this.xml.getElementsByTagName("CMD2").item(0));
		nodes.add(this.xml.getElementsByTagName("CMD3").item(0));
		tree.extend(this.xml.getElementsByTagName("ADAM").item(0), nodes);
		nodes = new ArrayList<Node>();
		nodes.add(this.xml.getElementsByTagName("CMD1").item(0));
		nodes.add(this.xml.getElementsByTagName("ADAM").item(0));
		nodes.add(this.xml.getElementsByTagName("CMD4").item(0));
		tree.extend(this.xml.getElementsByTagName("DT80").item(0), nodes);
		// Navigazione Albero
		DOMTreeNavigator navigator = new DOMTreeNavigator(NavigationMode.LEFTMOST);
		tree.navigate(navigator);
		logDOMElements(navigator.getXML(), this.log2);
		// Verifica
		assertEquals(log1.size(), this.log2.size());
		for (int i = 0; i < this.log1.size(); i++) {
			assertEquals(this.log2.get(i), this.log1.get(i));
		}
	}
	
	@Test
	public final void testSplit() {
		ExtensibleTree<String> tree = new ExtensibleTree<String>();
		String s1 = "1", s2 = "2", s3 = "3";
		tree.addBranch(s1, Arrays.asList(s2, s3));
		assertEquals(1, tree.split().size());
		String s0 = "0", sA = "A", sB = "B";
		tree.addBranch(s0, Arrays.asList(s1, sA, sB));
		assertEquals(1, tree.split().size());
		String s_ = "_";
		tree.addBranch(s_, Arrays.asList(s1));
		assertEquals(2, tree.split().size());
		// Prova Navigazione albero intero N.B. uso i log comuni...
		this.log1.clear();
		this.log1.add(s0);
		this.log1.add(s1);
		this.log1.add(s2);
		this.log1.add(s3);
		this.log1.add(sA);
		this.log1.add(sB);
		this.log1.add(s_);
		this.log1.add(s1);
		this.log1.add(s2);
		this.log1.add(s3);
		// uso log2 per navigare l'albero...
		this.log2.clear();
		StringTreeNavigator nav = new StringTreeNavigator(this.log2);
		tree.navigate(nav);
		// Verifica
		assertEquals(log1.size(), this.log2.size());
		for (int i = 0; i < this.log1.size(); i++) {
			assertEquals(this.log2.get(i), this.log1.get(i));
		}
	}
	
	@Test
	public final void testSplitNavigate() {
		ExtensibleTree<String> tree = new ExtensibleTree<String>();
		String s1 = "1", s2 = "2", s3 = "3";
		tree.addBranch(s1, Arrays.asList(s2, s3));
		String s0 = "0", sA = "A", sB = "B";
		tree.addBranch(s0, Arrays.asList(s1, sA, sB));
		String s_ = "_";
		tree.addBranch(s_, Arrays.asList(s1));
		// Ottengo alberi navigabili
		List<? extends NavigableTree<String>> trees; // TODO Usare LIST e applicare SEMPRE il concetto di lista.
		assertEquals(2, (trees = tree.split()).size());
		// Verifico navigazione primo albero:
		this.log1.clear();
		this.log1.add(s0);
		this.log1.add(s1);
		this.log1.add(s2);
		this.log1.add(s3);
		this.log1.add(sA);
		this.log1.add(sB);
		//
		this.log2.clear();
		StringTreeNavigator nav = new StringTreeNavigator(this.log2);
		trees.get(0).navigate(nav);
		// Verifica
		assertEquals(log1.size(), this.log2.size());
		for (int i = 0; i < this.log1.size(); i++) {
			assertEquals(this.log2.get(i), this.log1.get(i));
		}
		// Verifico navigazione secondo albero:
		this.log1.clear();
		this.log1.add(s_);
		this.log1.add(s1);
		this.log1.add(s2);
		this.log1.add(s3);
		//
		this.log2.clear();
		nav = new StringTreeNavigator(this.log2);
		trees.get(1).navigate(nav);
		// Verifica
		assertEquals(log1.size(), this.log2.size());
		for (int i = 0; i < this.log1.size(); i++) {
			assertEquals(this.log2.get(i), this.log1.get(i));
		}
	}
}