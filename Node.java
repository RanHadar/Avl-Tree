package oop.ex4.data_structures;

public class Node
{
    int myNumber; //node's data
    int height; //nodes' height
    Node leftChild; //node's left child
    Node rightChild;//node's right child
    Node parent; //node's parent

    /**
     * Default constructor for a Node
     * @param myNumber
     * @param parent
     */
    public Node(int myNumber,Node parent)
    {
        this.parent = parent;
        if(parent == null)//is it's the root
        {
            this.height = 0;
        }
        this.myNumber = myNumber;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Sets ths new height of the node considers his children.
     */
    public void setHeight()
    {
        if(rightChild != null && leftChild != null)
            this.height = (Math.max(rightChild.height,leftChild.height)+1);
        else if(rightChild == null && leftChild !=null)
            this.height = leftChild.height +1;
        else if(rightChild != null && leftChild == null)
            this.height = rightChild.height +1;
        else this.height = 0;
    }



}
