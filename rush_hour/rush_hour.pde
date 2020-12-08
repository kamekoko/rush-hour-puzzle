import java.util.Map;
import java.util.HashMap;

void setup() {
  size(1500, 1500);
  background(0);
  printState(input);
  println("start" + "\n");

  HashMap<String, Integer> hm = new HashMap<String, Integer>();
  int depth = 0;
  hm.put(convertStateToString(input), depth);
  // if (searchNextState(hm, input, depth)) ;
  int start = millis();
  while(true) {
    boolean finish = false;
    HashMap<String, Integer> subHm = new HashMap<String, Integer>();
    for (Map.Entry me : hm.entrySet()) {
      if (depth != int(me.getValue().toString())) continue;
      String str = "" + me.getKey();
      int[][] newState = convertStringToState(str);
      if (searchNextState(hm, subHm, newState,depth)) {
        finish = true;
        break;
      }
    }
    depth++;
    if (finish) break;
    for (Map.Entry me : subHm.entrySet()) {
      hm.put("" + me.getKey(), int(me.getValue().toString()));
    }
    if (subHm.size() <= 0) {
      println("no answer");
      break;
    }
  }
  println("count : " + depth);
  println("time : " + (millis() - start) + "ms");
  exit();
}

// void draw() {
//
// }
