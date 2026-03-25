import java.util.HashMap;


class AVLBinaryTree {

    Node root;

    static class Node {
        int score;
        String teamId;
        int height;
        Node left, right;

        Node(int score, String teamId) {
            this.score = score;
            this.teamId = teamId;
            this.height = 1;
        }

        //the compare command for the rotations by score then alpha, if the current
        //node has the same score it will sort alphabetically
        int compareTo(int s, String t) {
            if (this.score != s) return Integer.compare(this.score, s);
            return this.teamId.compareTo(t);
        }
    }

    //returns the height of the tree, null is 0 height
    public int height(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    //returns if the avl is outside the -1 1 range for it to be balanced
    //im using this to check in my insertion and deletion methods to prevent the tree from
    //going out of bounds and making it unstable
    public int getBalance(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }


    //Notes***
    //in the avl trees i need seperate functions to move the rotations right and left because
    //if i added that logic to every function this file would be double the size
    //the right rotation takes a node and switches it with the one to the right of it
    //will be used when the left subtree next to it is too tall
    //will be used with the getbalance function to perform the rotations
    public Node rightRotate(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    //same stuff as last func but moving left if right
    public Node leftRotate(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }


    //goes down tree recursively and finds correct spot for new info
    //after inputing new info it uses the compare balance and rotate functions to perform balancing
    //only need to check balance once and perform rotations after because it will have been balance prior to new entry

    Node Insert(Node node, int score, String teamId) {
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

        if (balance > 1 && node.left.compareTo(score, teamId) > 0)
            return rightRotate(node);
        if (balance < -1 && node.right.compareTo(score, teamId) < 0)
            return leftRotate(node);
        if (balance > 1 && node.left.compareTo(score, teamId) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && node.right.compareTo(score, teamId) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    //gets right most node which is the largest
    Node maxNode(Node node) {
        return node.right == null ? node : maxNode(node.right);
    }

    //searches for inputed team
    //deletes and replaces with successor
    //does same balancing check as insert
    Node Delete(Node node, int score, String teamId) {
        if (node == null) return null;

        int cmp = node.compareTo(score, teamId);
        if (cmp > 0)
            node.left = Delete(node.left, score, teamId);
        else if (cmp < 0)
            node.right = Delete(node.right, score, teamId);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node s = Successor(node, node.score, node.teamId, null);
            node.score = s.score;
            node.teamId = s.teamId;
            node.right = Delete(node.right, s.score, s.teamId);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    //checks the team map
    String Search(String teamId, HashMap<String, Integer> teams) {
        if (!teams.containsKey(teamId)) return "NOT FOUND";
        return teamId + " " + teams.get(teamId);
    }

    //goes down the lis to see the last biggest value
    //keep a best checker incase the last node goes only left
    Node Predecessor(Node node, int score, String teamId, Node best) {
        if (node == null) return best;
        if (node.compareTo(score, teamId) < 0)
            return Predecessor(node.right, score, teamId, node);
        else
            return Predecessor(node.left, score, teamId, best);
    }

    //goes down tree and see the next biggest value
    //have best again for same reason but instead right
    Node Successor(Node node, int score, String teamId, Node best) {
        if (node == null) return best;
        if (node.compareTo(score, teamId) > 0)
            return Successor(node.left, score, teamId, node);
        else
            return Successor(node.right, score, teamId, best);
    }

    //uses successor to find first team with score greater than or equal to low
    //goes until score is over hi variable
    //low - 1, \uffff is sent to suc func to find first good node
    Node[] RangeQuery(Node root, int low, int hi) {
        Node[] result = new Node[64];
        int count = 0;
        Node curr = Successor(root, low - 1, "\uFFFF", null);
        while (curr != null && curr.score <= hi) {
            result[count] = curr;
            count++;
            curr = Successor(root, curr.score, curr.teamId, null);
        }
        return result;
    }

    //start at max node and use predesescor to return all top values within given range
    int Top(Node root, int k, int collected, Node[] result) {
        Node curr = maxNode(root);
        while (curr != null && collected < k) {
            result[collected] = curr;
            collected++;
            curr = Predecessor(root, curr.score, curr.teamId, null);
        }
        return collected;
    }
}