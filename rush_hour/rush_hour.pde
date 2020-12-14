import java.util.Map;
import java.util.HashMap;

void setup() {

  for (int k = 13; k < 21; k++) {

    // input
    String filename = "board" + k;
    String inputfile = "../input/" + filename + ".txt";
    String[] txt = loadStrings(inputfile);
    GAME_SIZE = txt.length;
    int[][] startState = convertStringToState(txt);
    printState(startState);
    setGoal(startState);

    // output
    String savefile = "../output/breadth-first/" + filename + "-bfs.txt";
    PrintWriter output = createWriter(savefile);

    HashMap<String, Integer> hm = new HashMap<String, Integer>();
    HashMap<String, String> parentHm = new HashMap<String, String>();
    hm.put(convertStateToString(startState), 0);
    int depth = 0;

    // serch
    int repeat = 10;
    int start = millis();
    for (int i = 0; i < repeat; i++) {
      hm = new HashMap<String, Integer>();
      parentHm = new HashMap<String, String>();
      hm.put(convertStateToString(startState), 0);
      depth = search(hm, parentHm);
    }
    int time = millis() - start;

    // check path
    ArrayList<String> path = getPath(hm, parentHm);

    output.println("count : " + depth);
    output.println("node  : " + hm.size());
    output.println("time  : " + int(time / repeat) + "ms" + "\n");
    for (String s : path) output.println(s);
    output.flush();
    output.close();

  }

  println("finished");
  exit();
}

// void draw() {
//
// }
