package oop.ex4.data_structures;

//-----------------------------------------Imports-------------------------------------------

import java.util.Iterator;
import java.math.*;

public class AvlTree implements Iterable<Integer>
{

    //-----------------------------------------fields----------------------------------------

    private Node root; // the root of the tree
    private int numOfNodes; //number of elements in the tree
    private int NOT_FOUND = -1; //if the element not found in the tree

//--------------------------------------Constructors-----------------------------------------

    /**
     *  The default constructor.
     */
    public AvlTree()
    {
        this.root = null;

        numOfNodes = 0;
    }

    /**
     * A constructor that builds a new AVL tree containing all unique
     * values in the input
     * array.
     * @param data the values to add to tree
     */
    public AvlTree(int[]data)
    {

        this.root = null;

        numOfNodes = 0;
        for (Integer addValue : data)
        {
            if (addValue != null && this.contains(addValue) == NOT_FOUND)
                this.add(addValue);

        }
    }

    /**
     * A copy constructor that creates a deep copy of the given AvlTree.
     * The new tree
     * contains all the values of the given tree, but not necessarily in the same
     * structure.
     * @param avlTree an AVL tree.
     */
    public AvlTree(AvlTree avlTree)
    {
        for(Iterator<Integer> treeIter = avlTree.iterator(); treeIter.hasNext();)
        {
            add(treeIter.next());
        }

    }

//--------------------------------------Methods-----------------------------------------

    /**
     * A Helper method to add, Gets a root node and a key
     * and insert it to the tree if its not there
     * @param rootNode
     * @param newValue
     * @return The relevant node after rotations.
     */
    private Node addHelper(Node rootNode,int newValue)
    {
        if(rootNode==null)
            return new Node(newValue,null);
        //Searching for the relevant node
        if(newValue<rootNode.myNumber) {// case the value is smaller
            rootNode.leftChild = addHelper(rootNode.leftChild, newValue);
        }
        else if(newValue>rootNode.myNumber) {
            // case the value is bigger
            rootNode.rightChild = addHelper(rootNode.rightChild, newValue);
        }
        else {
            return null; //in case the value already in the tree
        }

        rootNode.setHeight();

        return CheckTreeBalance(rootNode);
    }

    /**
     * Add a new node with the given key to the tree
     * @param newValue the value of the new node to add.
     * @return true if the value to add is not already in the tree and it was
     * successfully added,
     * false otherwise.
     */
    public boolean add(int newValue)
    {

        Node tempNode = addHelper(this.root, newValue);
        if(tempNode!=null) //if the given value isn't in the tree.
        {
            this.root = tempNode;
            numOfNodes++;
            return true;
        }

    return false;// the given false is in the tree.
    }


    /**
     * Removes the node with the given value from the tree, if it exists
     * @param toDelete the value to remove from the tree.
     * @return true if the given value was found and deleted, false otherwise.
     */
    public boolean delete(int toDelete)
    {
        if(contains(toDelete)!=NOT_FOUND)
        {
            this.root = deleteHelper(root, toDelete);
            numOfNodes--;
            return true;
        }
        return false;

    }

    /**
     * A helper method to delete method, Gets a root node and the value to delete and
     * attempts to delete the value from the tree if not exist
     * @param rootNode
     * @param toDelete
     * @return Node
     */
    private Node deleteHelper(Node rootNode,int toDelete)
    {
        if(rootNode==null)
            return rootNode;

        //Searching for the relevant node
        if(toDelete<rootNode.myNumber)
            //if the value is smaller then the root's value
            rootNode.leftChild = deleteHelper(rootNode.leftChild,toDelete);
        else if(toDelete>rootNode.myNumber)
            //if the value is bigger then the root's value
            rootNode.rightChild = deleteHelper(rootNode.rightChild,toDelete);
        else
            {//if the root's value is equals to the value that need to be deleted
            if((rootNode.rightChild == null)||(rootNode.leftChild == null))
            {
                Node tempNode = null;
                if(tempNode ==rootNode.leftChild) //left child exist
                    tempNode=rootNode.rightChild;
                else
                    tempNode=rootNode.leftChild; //right child exist

                if(tempNode==null)//no child case
                {
                    tempNode =rootNode;
                    rootNode=null;
                }
                else//one child case
                    rootNode=tempNode;
            }
            else//node with two children
            {//taking the smallest node from the right subtree
                Node tempNode = treeMinimum(rootNode.rightChild);
                rootNode.myNumber = tempNode.myNumber;
                rootNode.rightChild =
                        deleteHelper(rootNode.rightChild,tempNode.myNumber);
            }
        }
        if(rootNode==null) //if the tree has only one node now don't continue checking
            return rootNode;

        rootNode.setHeight();
        return CheckTreeBalance(rootNode);//checks balance after deleting until the root
    }

    /**
     * Checks the tree balance for Avl tree for the given node and orders
     * the relevant rotate for balancing the tree if needed
     * @param curNode
     */
    private Node CheckTreeBalance(Node curNode)
    {
        int curBalance = getBalance(curNode);

            //left-left
        if (curBalance  > 1 && getBalance(curNode.leftChild) >= 0)
            return  rightRotate(curNode);

            // Left Right Case
        else if (curBalance  > 1 && getBalance(curNode.leftChild) < 0)
        {
             curNode.leftChild=(leftRotate(curNode.leftChild));
             return rightRotate(curNode);
        }

            // Right Right Case
        else if (curBalance  < -1 && getBalance(curNode.rightChild) <= 0)
            return leftRotate(curNode);


            // Right Left Case
        else if (curBalance  < -1 && getBalance(curNode.rightChild) > 0)
        {
            curNode.rightChild=(rightRotate(curNode.rightChild));
            return leftRotate(curNode);
        }
        return curNode;

    }

    /**
     * Responsible for doing right rotation when needed
     * @param rotateNode
     * @return The node that has been rotated to the position of the given node
     */
    private Node rightRotate(Node rotateNode)
    {
    //switching the pointers of the two elements
        Node tempNode1 = rotateNode.leftChild;
        Node tempNode2 = tempNode1.rightChild;

        tempNode1.rightChild=(rotateNode);
        rotateNode.leftChild=(tempNode2);

        tempNode1.parent =rotateNode.parent;
        rotateNode.parent = tempNode1;

        rotateNode.setHeight();
        tempNode1.setHeight();

        return tempNode1;
    }

    /**
     * Responsible for doing left rotation when needed
     * @param rotateNode
     * @return The node that has been rotated to the position of the given node
     */
    private Node leftRotate(Node rotateNode)
    {
        //switching the pointers of the two elements
        Node tempNode1 = rotateNode.rightChild;
        Node tempNode2 = tempNode1.leftChild;

        tempNode1.leftChild = rotateNode;
        rotateNode.rightChild = tempNode2;

        tempNode1.parent =rotateNode.parent;
        rotateNode.parent = tempNode1;

        rotateNode.setHeight();
        tempNode1.setHeight();

        return tempNode1;
    }

    /**
     *Check whether the tree contains the given input value
     * @param searchVal the value to search for.
     * @return the depth of the node (0 for the root) with the given value
     * if it was found in
     * the tree, −1 otherwise.
     */
    public int contains(int searchVal) {
        if (this.root != null)
        {
            Node tempRoot = this.root;
            int depth = 0;
            while (tempRoot != null) {
                if (searchVal == tempRoot.myNumber)
                    return depth;
                if (searchVal < tempRoot.myNumber)
                    tempRoot = tempRoot.leftChild;
                else
                    tempRoot = tempRoot.rightChild;

                depth++; //go deeper in the tree
            }
        }
        return NOT_FOUND; //if the element isn't in the tree
    }

    /**
     * Search for a specific Node in the tree
     * @param value
     * @param rootNode
     * @retun The node if found, null if not found.
     */
    private Node searchNode(int value, Node rootNode)
    {
        if(rootNode == null)
            return null;

        if (value<rootNode.myNumber)
            if(rootNode.leftChild != null)
                return searchNode(value,rootNode.leftChild);
            else
                return rootNode;

        else  if (value>rootNode.myNumber)
            if(rootNode.rightChild != null)
                return searchNode(value,rootNode.rightChild);
            else
                return rootNode;
        else
            return rootNode;

    }

    /**
     * Responsible for finding the successor node in the tree
     * @param node
     * @return successor node
     */
    private Node successor(Node node)
    {
        if(node.rightChild != null)
            return treeMinimum(node.rightChild);
        Node parentNode = node.parent;
        while ((parentNode!= null)&&(node ==parentNode.rightChild))
        {
            node = parentNode;
            parentNode = parentNode.parent;
        }
        return parentNode;

    }

    /**
     * @return the number of nodes in the tree
     */
    public int size()
    {
        return numOfNodes;
    }

    /**
     * Calculates the minimum number of nodes in an AVL tree of height h.
     * @param h  the height of the tree (a non−negative number) in question.
     * @return the minimum number of nodes in an AVL tree of the given height.
     */
    public static int findMinNodes(int h)
    {
        int minNodes = (int)(Math.round(((Math.sqrt(5)+2)/
                Math.sqrt(5))*Math.pow((1+
                Math.sqrt(5))/2,h)-1));

        return minNodes;
    }

    /**
     * @param node
     * @return the minimum node of the tree
     */
    private Node treeMinimum(Node node)
    {
        while(node.leftChild != null)
            node = node.leftChild;
        return node;
    }


    /**
     * Calculates the balance factor of a node
     * @param node
     * @return int - balance factor
     */
    private int getBalance(Node node)
    {
        if(node.leftChild != null && node.rightChild != null)
            return node.leftChild.height - node.rightChild.height;
        else if(node.leftChild != null && node.rightChild == null)
            return (node.leftChild.height - (-1));
        else if(node.leftChild == null && node.rightChild != null)
            return ((-1) - node.rightChild.height);
        else
            return -1;
    }

    /**
     * Finding the max number of nodes in the tree
     * @param h - height of the tree
     * @return int- max number
     */
    public static int findMaxNodes(int h)
    {
        return (int)Math.pow(2,h+1)-1;
    }

    //--------------------------------------Inner Class-----------------------------------------

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Integer> iterator() {


        class AvlTreeIterator implements Iterator<Integer> {
            private Node nextNode;

            private AvlTreeIterator(Node rootNode)
            {
                nextNode =treeMinimum(rootNode);

            }

            @Override
            public boolean hasNext()
            {
                //if the tree has next element
                return nextNode!=null;

            }


            @Override
            public Integer next()
            {
                if (hasNext())
                {
                    int NodeData = nextNode.myNumber;
                    nextNode = successor(nextNode);
                    return NodeData;
                }
                else
                    throw new IndexOutOfBoundsException();//no more elements
            }


            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();

            }
        }
        return new AvlTreeIterator(root); //return an iterator contains all
        // the elements from the tree
    }

    //------------------------------------------------------------------------------------



}

