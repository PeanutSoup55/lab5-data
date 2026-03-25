void main() {
    AVLBinaryTree tree = new AVLBinaryTree();
    HashMap<String, Integer> teams = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextLine()) {
        String line = sc.nextLine().trim();
        if (line.isEmpty()) continue;
        String[] p = line.split("\\s+");

        switch (p[0].toUpperCase()) {
            case "ADD":
                teams.put(p[1], Integer.parseInt(p[2]));
                tree.root = tree.Insert(tree.root, Integer.parseInt(p[2]), p[1]);
                break;

            case "UPDATE":
                int oldScore = teams.get(p[1]);
                tree.root = tree.Delete(tree.root, oldScore, p[1]);
                int newScore = Integer.parseInt(p[2]);
                teams.put(p[1], newScore);
                tree.root = tree.Insert(tree.root, newScore, p[1]);
                break;

            case "REMOVE":
                tree.root = tree.Delete(tree.root, teams.get(p[1]), p[1]);
                teams.remove(p[1]);
                break;

            case "LOOKUP":
                System.out.println(tree.Search(p[1], teams));
                break;

            case "PRED":
                int ps = teams.get(p[1]);
                Node pred = tree.Predecessor(tree.root, ps, p[1], null);
                System.out.println(pred == null ? "NONE" : pred.teamId + " " + pred.score);
                break;

            case "SUCC":
                int ss = teams.get(p[1]);
                Node succ = tree.Successor(tree.root, ss, p[1], null);
                System.out.println(succ == null ? "NONE" : succ.teamId + " " + succ.score);
                break;

            case "RANGE":
                int low = Integer.parseInt(p[1]);
                int high = Integer.parseInt(p[2]);
                System.out.println("RANGE " + low + " " + high);
                tree.RangeQuery(tree.root, low, high);
                break;

            case "TOP":
                int k = Integer.parseInt(p[1]);
                System.out.println("TOP " + k);
                tree.Top(tree.root, k, 0);
                break;
        }
    }
    sc.close();
}