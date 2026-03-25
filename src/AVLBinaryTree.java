public class AVLBinaryTree {

    private Node root;

    static class Node{
        int score, height;
        String teamID;
        Node left, right;
         Node(int score, String teamID){
             this.score = score;
             this.teamID = teamID;
             this.height = 1;
         }
        int compareTo(int s, String t) {
            if (this.score != s) return Integer.compare(this.score, s);
            return this.teamID.compareTo(t);
        }
    }

    int height(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    int getBalance(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    Node rightRotate(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    Node leftRotate(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    Node minNode(Node node) {
        return node.left == null ? node : minNode(node.left);
    }

    public Node Insert(Node node, int score, String teamId) {
        if (node == null)
            return new Node(score, teamId);

        int cmp = node.compareTo(score, teamId);
        if (cmp > 0)
            node.left = Insert(node.left, score, teamId);
        else if (cmp < 0)
            node.right = Insert(node.right, score, teamId);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && node.left.compareTo(score, teamId) > 0)
            return rightRotate(node);

        // Right Right
        if (balance < -1 && node.right.compareTo(score, teamId) < 0)
            return leftRotate(node);

        // Left Right
        if (balance > 1 && node.left.compareTo(score, teamId) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left
        if (balance < -1 && node.right.compareTo(score, teamId) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    public Node Delete(Node node, int score, String teamId) {
        if (node == null) return null;

        int cmp = node.compareTo(score, teamId);
        if (cmp > 0)
            node.left = Delete(node.left, score, teamId);
        else if (cmp < 0)
            node.right = Delete(node.right, score, teamId);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node s = minNode(node.right);
            node.score = s.score;
            node.teamId = s.teamId;
            node.right = Delete(node.right, s.score, s.teamId);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // Left Right
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // Right Left
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    public String Search(int key){
        String value = "";
        return value;
    }
    public Integer Predecessor(int key){
        return key;
    }
    public Integer Successor(int key){
        return key;
    }
    public AVLBinaryTree RangeQuery(int a, int b){

    }
}
