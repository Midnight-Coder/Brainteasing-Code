import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

/*
 * Typical Trie problem: build a Trie from Dictionary and use it to validate a grammatical word
 * Assume: English dictionary 
 */
/*
 * My approach:
 * Build a Trie with Hashmaps [O(1) access]; a bool indicates whether the particular node is a word in Dictionary
 */
class TrieNode {
	private HashMap<Character, TrieNode> children;
	private Character value;
	private boolean isWord;
	TrieNode(Character character, HashMap<Character, TrieNode> children, boolean isWord) {
		this.value = character;
		this.children = children;
		this.isWord = isWord;
	}
	public HashMap<Character, TrieNode> getChildren() {
		return children;
	}
	public void setChildren(HashMap<Character, TrieNode> c) {
		this.children = c;
	}
	public Character getValue() {
		return value;
	}
	public boolean isWord() {
		return isWord;
	}
	/*
	 * New words spring up all the time
	 */
	public void setWord(boolean b) {
		isWord = b;
	}
}
public class TrieDictionary {

	private static TrieNode root;
	public static void main(String[] args) {
		root = new TrieNode((char)0, null, false);
		createTrieFromWord("Wrong", root);
		createTrieFromWord("Good", root);
		print(root);
	}
	/*TODO remove printed words from the stringbuilder - or have the words be stored in the Node
	 * Or have a recursive procedure ->sb+=node.getValue() if (node isWord =>print) 
	 */
	private static void print(TrieNode root) {
		Stack<TrieNode> stack = new Stack<TrieNode>();
		StringBuilder validWord = new StringBuilder();
		stack.push(root);
		while(!stack.isEmpty()) {
			TrieNode temp = stack.pop();
			validWord.append(temp.getValue());
			if(temp.isWord())
				System.out.println(validWord);
			HashMap<Character,TrieNode> children = temp.getChildren();
			if(children == null) {
				
				continue;
			}
			for(Map.Entry<Character, TrieNode> e:children.entrySet()) {
				stack.add(e.getValue());
			}
		}		
	}
	static void createTrieFromWord(String word, TrieNode node) {
		int depth = word.length();
		word = word.toUpperCase();
		for(int i=0; i<depth; i++) {
			char ch = word.charAt(i);
			HashMap<Character, TrieNode> children = node.getChildren();
			if(children == null || !children.containsKey(ch)) {
				/*Create the nodes required for the new word => add children to pappa Nodes*/
				TrieNode temp = new TrieNode(ch, null, false);
				if(children==null) {
					children = new HashMap<Character,TrieNode>();
				}
				children.put(ch, temp);
				node.setChildren(children);
				node = temp;
			}
			else {
				node = children.get(ch);
			}
		}
		node.setWord(true);
	}
}
