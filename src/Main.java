void main() {
    AVLBinaryTree tree = new AVLBinaryTree();
    //make hashmap and then populate tree with it
    HashMap<String, Integer> teams = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextLine()) {
        String line = sc.nextLine().trim();
        if (line.isEmpty()) continue;
        //take input and split the string by using spaces for seperate input
        String[] p = line.split(" ");

        switch (p[0].toUpperCase()) {
            //the first split and secodn split represent the team and score on each use case


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

            case "RANGE":
                int low = Integer.parseInt(p[1]);
                int hi = Integer.parseInt(p[2]);
                System.out.println("RANGE " + low + " " + hi);
                AVLBinaryTree.Node[] range = tree.RangeQuery(tree.root, low, hi);
                for (int i = 0; i < range.length && range[i] != null; i++)
                    System.out.println(range[i].teamId + " " + range[i].score);
                break;

            case "TOP":
                int k = Integer.parseInt(p[1]);
                System.out.println("TOP " + k);
                AVLBinaryTree.Node[] top = new AVLBinaryTree.Node[k];
                int count = tree.Top(tree.root, k, 0, top);
                for (int i = 0; i < count; i++)
                    System.out.println(top[i].teamId + " " + top[i].score);
                break;
        }
    }
    sc.close();
}