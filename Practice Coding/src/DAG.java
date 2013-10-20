import java.util.ArrayList;
import java.util.List;


/*
 * Given a bunch of nodes :
 * 	1. 	Create a DAG
 * 	2. 	Functionality to evaluate weight of sub graph
 */
/*
 * Ex. ID	Parent_ID	Weight
 * 		2		1		10
 * 		3		2		20
 * 		1		0		15
 * 		50		48		30...
 * 
 * Orphan[]	-->	[3,20] --> [2,10] --> [1,15]
 * 			-->	[50,30] 
 */
/*
 * My Approach:
 * Maintain a list of orphans[] : 	Each orphan node is present in the list unless a parent is encountered
 * On node creation	: Traverse the DAG to find parent and children O(V) where |V| is the #nodes
 * 
 * Weight of Subgraph: Iterate through the orphans[] -> find node -> recursively add weight
 */
class Node {
	private int id, parentID, weight;
	private Node pappa;
	public Node(int id, int parentID, int weight, Node pappa) {
		this.id = id;
		this.parentID = parentID;
		this.weight = weight;
		this.pappa = pappa;
	}
	public Node(int id, int parentID, int weight) {
		this.id = id;
		this.parentID = parentID;
		this.weight = weight;
		this.pappa = null;
	}

	public int getId() {
		return id;
	}
	public int getParentID() {
		return parentID;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public Node getPappa() {
		return pappa;
	}
	public void setPappa(Node parent) {
		if(this.parentID != parent.getId()) {
			//Something is wrong. Not the correct parent.
			System.err.println("Parent ID " + this.parentID + " mismatch with the parent:" + parent.getId());
			throw new Error();
		}
		this.pappa = parent;
	}
	@Override
	public String toString() {
		String s = "[" + this.id + "," + this.weight + "]";
		return s;
	}
}
public class DAG {
	public static void main(String[] args) {
		List<Node> roots = new ArrayList<Node>();
		{
			createNode(roots, 2,1,10);
			createNode(roots, 3,2,15);
			createNode(roots, 1,0,25);
			createNode(roots, 50,4,3);
			
			System.out.println("E2:" + subGraphWeight(roots, 2));
		}
	}
	private static void createNode(List<Node> orphans, int id, int pappaId, int weight) {
		Node newBorn = new Node(id,pappaId,weight,null);
		if(orphans.size() == 0) {
			/*If there is no DAG -> don't look for parents */
			orphans.add(newBorn);
			return;
		}
		/*
		 * Traverse to search for parent. 
		 * Also look for orphan nodes
		 */
		boolean hasParent = false;
		Node child = null;
		for(int j=0; j<orphans.size(); j++) {	//**Advanced for loop or iterators perform CheckForModifcation()
			Node i = orphans.get(j);
			
			//Look for child nodes of newBorn
			if(child == null) {
				child = findParent(i,newBorn.getId());
				if(child != null) {
					child.setPappa(newBorn);
					continue;
				}
			}
			if(!hasParent && i.getId() == newBorn.getParentID()){
					newBorn.setPappa(i);
					hasParent = true;
					orphans.remove(i);  //Checks for presence and removes. Java :)
					/*Since the newBorn node stores the ref to the parent - the parent is no more an orphan */
			}
			
			if(child != null && hasParent) {
				break;
			}
		}
		if(child == null) {
			/*Since the newBorn's ref is not stored by any 'child' */
			orphans.add(newBorn);
		}
		print(orphans);
	}
	private static int subGraphWeight(List<Node>orphans, int id){
		Node subgraph = null;
		for(Node i:orphans) {
			subgraph = findNode(i, id);
			if(subgraph != null) {
				break;
			}
		}
		if(subgraph == null) {
			System.err.println("Node doesn't exist in the DAG");
			return -1;
		}
		return sum(subgraph);
	}
	private static int sum(Node n) {
		if(n == null)
			return 0;
		return n.getWeight() + sum(n.getPappa());
	}
	private static void print(List<Node> orphans) {
		for(Node i:orphans) {
			do {
				System.out.print(i + " --> ");
				i = i.getPappa();
			}while(i!=null);
			System.out.println("null");
		}
		System.out.println();
		System.out.println();
	}
	private static Node findNode(Node n, int id) {
		if(n == null)
			return null;
		if(n.getId() == id)
			return n;
		else
			return findNode(n.getPappa(), id);
	}
	private static Node findParent(Node n, int parentId) {
		if(n == null)
			return null;
		if(n.getParentID() == parentId)
			return n;
		else
			return findParent(n.getPappa(),parentId);
	}
}
