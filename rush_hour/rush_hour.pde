import java.util.Map;
import java.util.HashMap;

void setup() {

  // input
  String filename = "board12.txt";
  String inputfile = "input/" + filename;
  String[] txt = loadStrings(inputfile);
  GAME_SIZE = txt.length;
  int[][] startState = convertStringToState(txt);
  printState(startState);
  setGoal(startState);

  // output
  String savefile = "output/" + filename;
  PrintWriter output = createWriter(savefile);

  HashMap<String, Integer> hm = new HashMap<String, Integer>();
  HashMap<String, String> parentHm = new HashMap<String, String>();
  hm.put(convertStateToString(startState), 0);

  // serch
  int start = millis();
  int depth = search(hm, parentHm);
  int time = millis() - start;

  // check path
  ArrayList<String> path = getPath(hm, parentHm);

  output.println("count : " + depth);
  output.println("node  : " + hm.size());
  output.println("time  : " + time + "ms" + "\n");
  for (String s : path) output.println(s);
  output.flush();
  output.close();

  println("finished");
  exit();
}

// void draw() {
//
// }
