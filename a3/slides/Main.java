package oswego.csc365.a3;

public class Main 
{
    public static void main( String[] args ) throws java.lang.reflect.InvocationTargetException {
        BTree bt = new BTree(3);
        bt.insert("a"); 
        bt.insert("c"); 
        bt.insert("g"); 
        bt.insert("j"); 
        bt.insert("k"); 
        bt.insert("m"); 
        bt.insert("n"); 
        bt.insert("o");
        bt.insert("r");
        bt.insert("p"); 
        bt.insert("s"); 
        bt.insert("x"); 
        bt.insert("y"); 
        bt.insert("z"); 
        bt.insert("u"); 
        bt.insert("d"); 
        bt.insert("q"); 
        bt.insert("e");
        bt.insert("t");
        bt.insert("v"); 
        bt.insert("b"); 
        bt.insert("l");
        bt.insert("f");

        System.out.println("Traversal of tree");
        bt.traverse(); 

        bt.remove("f");
        System.out.println("Traversal of tree after removing f");
        bt.traverse(); 

        bt.remove("m");
        System.out.println("Traversal of tree after removing m");
        bt.traverse(); 

        bt.remove("g");
        System.out.println("Traversal of tree after removing m");
        bt.traverse();

        bt.remove("d");
        System.out.println("Traversal of tree after removing m");
        bt.traverse(); 

        bt.remove("b");
        System.out.println("Traversal of tree after removing m");
        bt.traverse();  

        bt.remove("p");
        System.out.println("Traversal of tree after removing m");
        bt.traverse();
    }
}
