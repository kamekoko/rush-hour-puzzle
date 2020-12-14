void setup() {
  String savefile = "results.csv";
  PrintWriter output = createWriter(savefile);
  output.println("input,bfs-depth,dfs-depth,bfs-time,dfs-time");

  for (int k = 1; k < 21; k++) {
    // input
    String filename = "board" + k;
    String bfsfile = filename + "-bfs.txt";
    String dfsfile = filename + "-dfs.txt";
    String[] bfstxt = loadStrings(bfsfile);
    String[] dfstxt = loadStrings(dfsfile);

    output.print(filename + ",");

    int bfsDepth = int(bfstxt[0].split(" : ", 0)[1]);
    int dfsDepth = int(dfstxt[0].split(" : ", 0)[1]);
    int bfsTime = int(bfstxt[2].split(" : ", 0)[1].split("ms",0)[0]);
    int dfsTime = int(dfstxt[2].split(" : ", 0)[1].split("ms",0)[0]);

    output.println(bfsDepth + "," + dfsDepth + "," + bfsTime + "," + dfsTime);
  }

  output.flush();
  output.close();
  exit();
}
