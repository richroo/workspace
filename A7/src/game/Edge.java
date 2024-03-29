package game;
import gui.Line;

import java.awt.Color;
import java.util.*;

/** Class Edge allows creation of  connections between Nodes.
 * Each Edge is bidirectional and is connected to exactly two Nodes. 
 * Functions getFirstExit() and getSecondExit()  allow access to these Nodes. 
 * Each Edge is weighted (has a length). <br><br>
 * 
 * Class Edge implements BoardElement and is represented on the threads by an
 * instance of class Line.
 * <br><br>
 * 
 * There are no public Edge constructors, so classes outside of the game package
 * cannot construct additional Edge objects.
 * 
 * @author MPatashnik
 *
 */
public final class Edge implements BoardElement{
    /**Max val an edge can have for length. */
    public static final int DEFAULT_MIN_LENGTH = Integer.MAX_VALUE;	

    /** Min val an edge can have for length. */
    public static final int DEFAULT_MAX_LENGTH = 0;

    private Node[] exits;	//The Nodes to which this edge connects. Always length 2.

    /** The length (weight) of this Edge. Uncorrelated with its graphical length on the GUI */
    public final int length;		

    private Object userData; //User data (if any) stored in this edge

    private Line line; //Graphical representation of this Edge

    private final Board board;  //The board this Edge belongs to

    /** Constructor. an Edge on m with end nodes in exits and length lengthOfRoad,
     * which must be positive and non-zero.
     * @throws IllegalArgumentException:
     * 		if exits is null or has length not equal to 2
     * 		if lengthOfRoad is less than 1 and not equal to Edge.DUMMY_LENGTH
     * 		if either of the nodes are null
     * 		if exits[0] and exits[1] are the same node*/
    protected Edge(Board m, Node exits[], int lengthOfRoad) throws IllegalArgumentException{
        this(m, exits[0], exits[1], lengthOfRoad);

        if (exits.length != 2)
            throw new IllegalArgumentException("Incorrectly sized Node Array Passed into Edge Constructor");
    }

    /** Constructor. An edge with end nodes firstExit and secondExit and  length
     * lengthOfRoad, which must be positive and non-zero.
     * @throws IllegalArgumentException:
     * 		if lengthOfRoad is less than 1 and not equal to Edge.DUMMY_LENGTH
     * 		if either of the nodes are null
     * 		if firstExit and secondExit are the same node*/
    protected Edge(Board m, Node firstExit, Node secondExit, int lengthOfRoad) 
            throws IllegalArgumentException {
        Node[] e = new Node[2];
        e[0] = firstExit;
        e[1] = secondExit;
        setExits(e);
        
        board= m;

        if (lengthOfRoad <= 0)
            throw new IllegalArgumentException("lengthOfRoad value " + lengthOfRoad + 
                    " is an illegal value.");

        length = lengthOfRoad;
        line = new Line(firstExit.getCircle(), secondExit.getCircle(), this);
    }

    /** Return the Board to which this Edge belongs. */
    @Override
    public Board getBoard() {
        return board;
    }

    /** Return the exits of this line, a length 2 array.
     * Order of nodes in returned array has no significance. */
    protected Node[] getTrueExits() {
        return exits;
    }

    /** Return the first exit of this Edge. Which node is the first exit has no significance. */
    public Node getFirstExit() {
        return exits[0];
    }

    /** Return the second exit of this Edge. Which node is the second exit has no significance. */
    public Node getSecondExit() {
        return exits[1];
    }

    /** Return a copy of the exits of this line, a new length 2 array of Nodes.
     * Copies the nodes into a new array to prevent interference with the exits of this node.
     * (Setting the values of the return of this method
     * will not alter the Edge object.)
     */
    public Node[] getExits() {
        Node[] newExits = new Node[2];
        newExits[0] = exits[0];
        newExits[1] = exits[1];
        return newExits;
    }

    /** Set the exists of this edge to newExits. Used only in edge construction.
     * @param newExits - the new exits to set.
     * @throws IllegalArgumentException:
     * 		if exits is null or has length not equal to 2
     * 		if either of the nodes are null
     * 		if exits[0] and exits[1] are the same node
     **/
    private void setExits(Node[] newExits) throws IllegalArgumentException{
        if (newExits == null)
            throw new IllegalArgumentException("Null Node Array Passed into Edge Constructor");

        if (newExits.length != 2)
            throw new IllegalArgumentException("Incorrectly sized Node Array Passed into Edge Constructor");

        if (newExits[0] == null)
            throw new IllegalArgumentException("First Node Passed into Edge constructor is null");

        if (newExits[1] == null)
            throw new IllegalArgumentException("Second Node Passed into Edge constructor is null");

        if (newExits[0].equals(newExits))
            throw new IllegalArgumentException("Two Nodes Passed into Edge constructor refer to the same node");

        exits = newExits;
    }

    /** Return true iff node is one of the exits of this Edge. */
    public boolean isExit(Node node) {
        return exits[0].equals(node) || exits[1].equals(node);
    }

    /** Return true iff this Edge and r share an exit. */
    public boolean sharesExit(Edge r) {
        if (exits[0].equals(r.getTrueExits()[0]))
            return true;
        if (exits[0].equals(r.getTrueExits()[1]))
            return true;
        if (exits[1].equals(r.getTrueExits()[0]))
            return true;
        if (exits[1].equals(r.getTrueExits()[1]))
            return true;

        return false;
    }

    /** Return the other exit that is not equal to n.
     *  (Return null if n is neither of the nodes in exits.) */
    public Node getOther(Node n) {
        if (exits[0].equals(n))
            return exits[1];
        if (exits[1].equals(n))
            return exits[0];

        return null;
    }

    /** Return the Line that represents this edge graphically. */
    public Line getLine() {
        return line;
    }

    /** Return the userData stored in this edge.
     * May be null if the user has not yet given this Node userData. */
    @Override
    public Object getUserData() {
        return userData;
    }

    /** Set the value of userData to uData.
     * To erase the current userData use null as the argument. */
    @Override
    public void setUserData(Object uData) {
        userData = uData;
    }

    /** Return the color of this Edge, as it is painted on the GUI. Color
     * of edges has no game significance, so this value may be changed during gameplay.
     */
    @Override
    public Color getColor() {
        return line.getColor();
    }
    
    /** Return false - the color of Edges is not significant */
    @Override
    public boolean isColorSignificant(){
    	return false;
    }

    /** Return true iff this edge and e are equal.
     * Two Edges are equal if they have the same exits, even if they have different lengths.
     * This ensures that only one edge connects each pair of nodes in duplicate-free collections.  */
    @Override
    public boolean equals(Object e) {
        if (e == null)
            return false;
        if (! (e instanceof Edge) )
            return false;
        Edge e1= (Edge) e;
        Node[] exist1= e1.getTrueExits();
        return (exits[0].equals(exist1[1]) && exits[1].equals(exist1[0])) ||
               (exits[0].equals(exist1[0]) && exits[1].equals(exist1[1]));
    }

    /** Return the hash code for this edge.
     * The hashCode is equal to the sum of the hashCodes of its first and second exit.
     * {@code getFirstExit().hashCode() + getSecondExit().hashCode()}.
     * Notably: This means the ordering of the exits for an edge doesn't matter for hashing */
    @Override
    public int hashCode() {
        return exits[0].hashCode() + exits[1].hashCode();
    }

    /** Return a String representation of this edge:
     * {@code getFirstExit().name + " to " + getSecondExit().name} */
    @Override
    public String toString() {
        return exits[0].name + " to " + exits[1].name; 
    }

    /** Return the  exits and length for an edge's JSON string */
    @Override
    public String toJSONString() {
        return "{\n" + Main.addQuotes(BoardElement.LOCATION_TOKEN) + ":[" 
                + Main.addQuotes(exits[0].name) + "," + Main.addQuotes(exits[1].name) + "]," +
                "\n" + Main.addQuotes(BoardElement.LENGTH_TOKEN) + ":" + length + 
                "\n}";
    }

    /** Return a String to print when this object is drawn on a GUI */
    @Override
    public String getMappedName() {
        return "" + length;
    }

    /** Return the x location the boardped name of this Edge relative to the top
     * left corner of the line */
    @Override
    public int getRelativeX() {
        return line.getXMid() - Math.min(line.getX1(), line.getX2()) + Line.LINE_THICKNESS;
    }

    /** Returns the y location the boardped name of this Edge relative to the top
     * left corner of the line */
    @Override
    public int getRelativeY() {
        return line.getYMid() - Math.min(line.getY1(), line.getY2()) + Line.LINE_THICKNESS*3;
    }


    /** Repaint the edge (the line). Parameters x and y unused but are included
     * to comply with interface. */
    @Override
    public void updateGUILocation(int x, int y) {
        getLine().fixBounds();
        getLine().repaint();
    }
}
