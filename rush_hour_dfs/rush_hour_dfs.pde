import java.util.Map;
import java.util.HashMap;

void setup() {

  for (int k = 1; k <= 20; k++) {

    // input
    String filename = "board" + k;
    String inputfile = "../input/" + filename + ".txt";
    String[] txt = loadStrings(inputfile);
    GAME_SIZE = txt.length;
    int[][] startState = convertStringToState(txt);
    printState(startState);
    setGoal(startState);

    // output
    String savefile = "output/priority/" + filename + ".txt";
    PrintWriter output = createWriter(savefile);

    HashMap<String, Integer> hm = new HashMap<String, Integer>();
    HashMap<String, String> parentHm = new HashMap<String, String>();
    int depth = 0;

    // serch
    int repeat = 10;
    int start = millis();
    for (int i = 0; i < repeat; i++) {
      hm = new HashMap<String, Integer>();
      parentHm = new HashMap<String, String>();
      depth = search(hm, parentHm, startState);
    }
    int time = millis() - start;

    // check path
    ArrayList<String> path = getPath(hm, parentHm);

    output.println("depth : " + depth);
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
