import java.util.HashMap;
class Node {
    int score;
    String teamId;
    int height;
    Node left, right;

    Node(int score, String teamId) {
        this.score = score;
        this.teamId = teamId;
        this.height = 1;
    }

    int compareTo(int s, String t) {
        if (this.score != s) return Integer.compare(this.score, s);
        return this.teamId.compareTo(t);
    }
}

class AVLBinaryTree {

    Node root;

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

    Node minNode(Node node) {
        return node.left == null ? node : minNode(node.left);
    }

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

    String Search(String teamId, java.util.HashMap<String, Integer> teams) {
        if (!teams.containsKey(teamId)) return "NOT FOUND";
        return teamId + " " + teams.get(teamId);
    }

    Node Predecessor(Node node, int score, String teamId, Node best) {
        if (node == null) return best;
        if (node.compareTo(score, teamId) < 0)
            return Predecessor(node.right, score, teamId, node);
        else
            return Predecessor(node.left, score, teamId, best);
    }

    Node Successor(Node node, int score, String teamId, Node best) {
        if (node == null) return best;
        if (node.compareTo(score, teamId) > 0)
            return Successor(node.left, score, teamId, node);
        else
            return Successor(node.right, score, teamId, best);
    }

    void RangeQuery(Node node, int lo, int hi) {
        if (node == null) return;
        if (node.score > lo) RangeQuery(node.left, lo, hi);
        if (node.score >= lo && node.score <= hi)
            System.out.println(node.teamId + " " + node.score);
        if (node.score < hi) RangeQuery(node.right, lo, hi);
    }

    int Top(Node node, int k, int printed) {
        if (node == null || printed >= k) return printed;
        printed = Top(node.right, k, printed);
        if (printed < k) {
            System.out.println(node.teamId + " " + node.score);
            printed++;
        }
        if (printed < k) printed = Top(node.left, k, printed);
        return printed;
    }
}