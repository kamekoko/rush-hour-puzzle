import java.util.Map;
import java.util.HashMap;

void setup() {

  // input
  String filename = "board2.txt";
  String inputfile = "input/" + filename;
  String[] txt = loadStrings(inputfile);
  int[][] startState = convertStringToState(txt);
  printState(startState);
  T = getTarget(startState);

  // output
  String savefile = "output/" + filename;
  PrintWriter output = createWriter(savefile);

  HashMap<String, Integer> hm = new HashMap<String, Integer>();
  HashMap<String, String> parentHm = new HashMap<String, String>();
  int depth = 0;
  hm.put(convertStateToString(startState), depth);

  // serch
  int start = millis();

  while(true) {
    boolean finish = false;
    HashMap<String, Integer> subHm = new HashMap<String, Integer>();
    for (Map.Entry me : hm.entrySet()) {
      if (depth != int(me.getValue().toString())) continue;
      String str = "" + me.getKey();
      int[][] newState = convertStringToState(str);
      if (searchNextState(hm, subHm, parentHm, newState, depth)) {
        finish = true;
        break;
      }
    }
    for (Map.Entry me : subHm.entrySet()) {
      hm.put("" + me.getKey(), int(me.getValue().toString()));
    }
    depth++;
    if (finish) break;
    if (subHm.size() <= 0) {
      output.println("no answer");
      println("no answer");
      break;
    }
  }

  int time = millis() - start;
  println("finished");

  // check path
  ArrayList<String> path = new ArrayList<String>();
  for (Map.Entry me : hm.entrySet()) {
    String str = me.getKey().toString();
    int[][] state = convertStringToState(str);
    if (isFinish(state, GOAL_COLUMN, T)) {
      path.add(str);
      while(true) {
        if (! parentHm.containsKey(str)) break;
        String parentStr = parentHm.get(str);
        path.add(0, parentStr);
        str = parentStr;
      }
    }
  }

  output.println("count : " + depth);
  output.println("node  : " + hm.size());
  output.println("time  : " + time + "ms" + "\n");
  for (String s : path) output.println(s);
  output.flush();
  output.close();

  exit();
}

// void draw() {
//
// }
