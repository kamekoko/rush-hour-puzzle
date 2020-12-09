import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Map; 
import java.util.HashMap; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class rush_hour extends PApplet {




public void setup() {

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
// Game
int GAME_SIZE;
int GOAL_COLUMN;
int T; // target

// search
final int RIGHT = 0;
final int LEFT = 1;
final int UP = 2;
final int DOWN = 3;
public String convertStateToString(int[][] state) {
  String str = new String();
  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      str += (state[i][j] + ",");
    }
    str += "\n";
  }
  return str;
}

public int[][] convertStringToState(String str) {
  int[][] state = new int[GAME_SIZE][GAME_SIZE];
  int address = 0;
  String[] column = split(str, "\n");
  for (String s : column) {
    String[] data = split(s, ",");
    for (String ss : data) {
      if (ss.length() <= 0) continue;
      int i = address / GAME_SIZE;
      int j = address % GAME_SIZE;
      state[i][j] = PApplet.parseInt(ss);
      address++;
    }
  }
  return state;
}

public int[][] convertStringToState(String[] str) {
  int[][] state = new int[GAME_SIZE][GAME_SIZE];

  for (int i = 0; i < str.length; i++) {
    String[] data = split(str[i], ",");
    for (int j = 0; j < data.length - 1; j++) {
      state[i][j] = PApplet.parseInt(data[j]);
    }
  }
  return state;
}
public void setGoal(int[][] state) {
  int max = 0;
  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      int val = state[i][j];
      if (max < val) {
        max = val;
        GOAL_COLUMN = i;
      }
    }
  }
  GAME_SIZE = state.length;
  T = max;
}

public int search(HashMap<String, Integer> hm, HashMap<String, String> parentHm) {
  int depth = 0;
  while(true) {
    boolean finish = false;
    HashMap<String, Integer> subHm = new HashMap<String, Integer>();
    for (Map.Entry me : hm.entrySet()) {
      if (depth != PApplet.parseInt(me.getValue().toString())) continue;
      String str = "" + me.getKey();
      int[][] newState = convertStringToState(str);
      if (searchNextState(hm, subHm, parentHm, newState, depth)) {
        finish = true;
        break;
      }
    }
    for (Map.Entry me : subHm.entrySet()) {
      hm.put("" + me.getKey(), PApplet.parseInt(me.getValue().toString()));
    }
    depth++;
    if (finish) break;
    if (subHm.size() <= 0) {
      println("no answer");
      break;
    }
  }
  return depth;
}

// boolean searchNextState(HashMap hm, HashMap subHm, HashMap parentHm, int[][] state, int depth) { //Hash h
//   for (int i = 0; i < state.length; i++) {
//     for (int j = 0; j < state.length; j++) {
//       if (state[i][j] == 0) continue;
//       if (isMovableTo(state, i, j, RIGHT)) {
//         int[][] copy = copyState(state);
//         moveTo(copy, i, state[i][j], RIGHT);
//         if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
//         if (isFinish(copy, GOAL_COLUMN, T)) return true;
//       }
//       if (isMovableTo(state, i, j, LEFT)) {
//         int[][] copy = copyState(state);
//         moveTo(copy, i, state[i][j], LEFT);
//         if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
//         if (isFinish(copy, GOAL_COLUMN, T)) return true;
//       }
//       if (isMovableTo(state, i, j, DOWN)) {
//         int[][] copy = copyState(state);
//         moveTo(copy, j, state[i][j], DOWN);
//         if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
//         if (isFinish(copy, GOAL_COLUMN, T)) return true;
//       }
//       if (isMovableTo(state, i, j, UP)) {
//         int[][] copy = copyState(state);
//         moveTo(copy, j, state[i][j], UP);
//         if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
//         if (isFinish(copy, GOAL_COLUMN, T)) return true;
//       }
//     }
//   }
//   return false;
// }
//
boolean one = false;

public boolean searchNextState(HashMap hm, HashMap subHm, HashMap parentHm, int[][] state, int depth) { //Hash h
  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      if (state[i][j] == 0) continue;
      if (isMovableTo(state, i, j, RIGHT)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i, j + slideNum, RIGHT)) {
          moveTo(copy, i, copy[i][j + slideNum], RIGHT);
          if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
          if (isFinish(copy, GOAL_COLUMN, T)) return true;
          slideNum++;
        }
      }
      if (isMovableTo(state, i, j, LEFT)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i, j - slideNum, LEFT)) {
          moveTo(copy, i, copy[i][j - slideNum], LEFT);
          if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
          // if (isFinish(copy, GOAL_COLUMN, T)) return true;
          slideNum++;
        }
      }
      if (isMovableTo(state, i, j, DOWN)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i + slideNum, j, DOWN)) {
          moveTo(copy, j, copy[i + slideNum][j], DOWN);
          if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
          // if (isFinish(copy, GOAL_COLUMN, T)) return true;
          slideNum++;
        }
      }
      if (isMovableTo(state, i, j, UP)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i - slideNum, j, UP)) {
          moveTo(copy, j, copy[i - slideNum][j], UP);
          if (put(hm, subHm, copy, depth)) putParent(parentHm, state, copy);
          // if (isFinish(copy, GOAL_COLUMN, T)) return true;
          slideNum++;
        }
      }
    }
  }
  return false;
}

public boolean isFinish(int[][] state, int column, int target) {
  return (state[column][state.length - 1] == target);
}

public boolean isMovableTo(int[][] state, int i, int j, int direction) {
  if (direction == RIGHT) {
    if (j == 0 || j == GAME_SIZE - 1) return false;
    if (state[i][j + 1] != 0 || state[i][j - 1] != state[i][j]) return false;
  }
  else if (direction == LEFT) {
    if (j == 0 || j == GAME_SIZE - 1) return false;
    if (state[i][j - 1] != 0 || state[i][j + 1] != state[i][j]) return false;
  }
  else if (direction == DOWN) {
    if (i == 0 || i == GAME_SIZE - 1) return false;
    if (state[i + 1][j] != 0 || state[i - 1][j] != state[i][j]) return false;
  }
  else if (direction == UP) {
    if (i == 0 || i == GAME_SIZE - 1) return false;
    if (state[i - 1][j] != 0 || state[i + 1][j] != state[i][j]) return false;
  }
  return true;
}

public boolean put(HashMap hm, HashMap subHm, int[][] state, int depth) {
  String str = convertStateToString(state);
  if (hm.containsKey(str) || subHm.containsKey(str)) return false;
  subHm.put(str, depth + 1);
  return true;
}

public int[][] copyState(int[][] s) {
  int[][] ans = new int[s.length][s[0].length];
  for (int i = 0; i < s.length; i++) {
    for (int j = 0; j < s[i].length; j++) {
      ans[i][j] = s[i][j];
    }
  }
  return ans;
}

public void putParent(HashMap parentHm, int[][] parentState, int[][] newState) {
  String parentStr = convertStateToString(parentState);
  String newStr = convertStateToString(newState);
  parentHm.put(newStr, parentStr);
}

public void moveTo(int[][] state, int line, int car, int direction) {
  if (direction == RIGHT) {
    for (int k = state.length - 1; k >= 0; k--) {
      if (k == 0) {
        if (state[line][k] == car) state[line][k] = 0;
      }
      else {
        int s = state[line][k];
        int ss = state[line][k - 1];
        if (s == 0 && ss == car) state[line][k] = car;
        if (s == car && ss != car) state[line][k] = 0;
      }
    }
  }

  else if (direction == LEFT) {
    for (int k = 0; k < state.length; k++) {
      if (k == state.length -1) {
        if (state[line][k] == car) state[line][k] = 0;
      }
      else {
        int s = state[line][k];
        int ss = state[line][k + 1];
        if (s == 0 && ss == car) state[line][k] = car;
        if (s == car && ss != car) state[line][k] = 0;
      }
    }
  }

  else if (direction == DOWN) {
    for (int k = state.length - 1; k >= 0; k--) {
      if (k == 0) {
        if (state[k][line] == car) state[k][line] = 0;
      }
      else {
        int s = state[k][line];
        int ss = state[k - 1][line];
        if (s == 0 && ss == car) state[k][line] = car;
        if (s == car && ss != car) state[k][line] = 0;
      }
    }
  }

  else if (direction == UP) {
    for (int k = 0; k < state.length; k++) {
      if (k == state.length - 1) {
        if (state[k][line] == car) state[k][line] = 0;
      }
      else {
        int s = state[k][line];
        int ss = state[k + 1][line];
        if (s == 0 && ss == car) state[k][line] = car;
        if (s == car && ss != car) state[k][line] = 0;
      }
    }
  }
}

public ArrayList<String> getPath(HashMap<String, Integer> hm, HashMap<String, String> parentHm) {
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
  return path;
}

public void printState(int[][] state) {
  for (int[] s : state) {
    for (int ss : s) print(ss + ",");
    println();
  }
  println();
}

// void checkPath(HashMap hm, HashMap pathHm) {
//   ArrayList<String> path = new ArrayList<String>();
//
//   for (Map.Entry me : hm.entrySet()) {
//     String str = me.getKey().toString();
//     int[][] state = convertStringToState(str);
//     if (isFinish(state, GOAL_COLUMN, T)) {
//       path.add(str);
//       while(true) {
//         if (! parentHm.containsKey(str)) break;
//         for (Map.Entry me2 : pathHm.entrySet()) {
//           if (me.getKey == me2.getKey) {
//             String parentStr = me2.getValue.toString();
//             path.add(0, parentStr);
//             str = parentStr;
//             break;
//           }
//         }
//       }
//     }
//   }
//
//   for (String s : path) {
//     printState(convertStringToState(s));
//   }
// }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "rush_hour" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
