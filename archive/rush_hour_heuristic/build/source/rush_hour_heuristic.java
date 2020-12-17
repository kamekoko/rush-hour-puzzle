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

public class rush_hour_heuristic extends PApplet {




public void setup() {

  for (int k = 19; k < 20; k++) {

    // input
    String filename = "board" + k;
    String inputfile = "input/" + filename + ".txt";
    String[] txt = loadStrings(inputfile);
    GAME_SIZE = txt.length;
    int[][] startState = convertStringToState(txt);
    printState(startState);
    setGoal(startState);

    // output
    String savefile = "../output/" + filename + "-dfs.txt";
    PrintWriter output = createWriter(savefile);

    HashMap<String, Integer> hm = new HashMap<String, Integer>();
    HashMap<String, String> parentHm = new HashMap<String, String>();
    // hm.put(convertStateToString(startState), 0);
    int depth = 0;

    // serch
    int repeat = 10;
    int start = millis();
    for (int i = 0; i < repeat; i++) {
      hm = new HashMap<String, Integer>();
      parentHm = new HashMap<String, String>();
      // hm.put(convertStateToString(startState), 0);
      depth = search(hm, parentHm, startState);
    }
    int time = millis() - start;

    // check path
    ArrayList<String> path = getPath(hm, parentHm);

    output.println("depth : " + depth);
    output.println("node  : " + hm.size());
    output.println("time  : " + PApplet.parseInt(time / repeat) + "ms" + "\n");
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
class Board {
  int[][] state;
  Board parent;

  Board(int[][] s) {
    state = s;
    parent = null;
  }
  Board(int[][] s, Board b) {
    state = s;
    parent = b;
  }

  public boolean isFinish() {
    return (state[GOAL_COLUMN][state.length - 1] == T);
  }

  public String toString() {
    String str = new String();
    for (int[] s : state) {
      for (int ss : s) str += (ss + ",");
      str += "\n";
    }
    return str + "\n";
  }
}
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
final int UNDISCOVERED = 0;
final int DISCOVERED = 1;
final int UPDATE = 2;

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


// 深さ優先探索
// 深さの管理に要注意，というか変更が必要
public int search(HashMap<String, Integer> hm, HashMap<String, String> parentHm, int[][] startState) {
  hm.put(convertStateToString(startState), 0);
  if (searchNextState(hm, parentHm, startState, 0)) return getDepth(hm);
  println("no answer");
  return 0;
}

public boolean searchNextState(HashMap hm, HashMap parentHm, int[][] state, int depth) {
  //if (isFinish(state, GOAL_COLUMN, T)) return true;
  if (nextIsGoal(hm, parentHm, state, depth)) return true;

  //if (depth > 3000) return false;
   for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      if (state[i][j] == 0) continue;
      if (isMovableTo(state, i, j, RIGHT)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i, j + slideNum, RIGHT)) {
          moveTo(copy, i, copy[i][j + slideNum], RIGHT);
          int newState = put(hm, copy, depth);
          if (newState != DISCOVERED) {
            putParent(parentHm, state, copy);
            if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
          }
          slideNum++;
        }
        // int w = getCarWidth(copy, i, j);
        // while (isMovableTo(copy, i, j + slideNum, RIGHT)) {
        //   moveTo(copy, i, copy[i][j + slideNum], RIGHT);
        //   slideNum++;
        // }
        // while (slideNum > 0) {
        //   int newState = put(hm, copy, depth);
        //   if (newState != DISCOVERED) {
        //     putParent(parentHm, state, copy);
        //     if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
        //   }
        //   moveTo(copy, i, copy[i][j + slideNum + 1 - w], LEFT);
        //   slideNum--;
        // }
      }
      if (isMovableTo(state, i, j, LEFT)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i, j - slideNum, LEFT)) {
          moveTo(copy, i, copy[i][j - slideNum], LEFT);
          int newState = put(hm, copy, depth);
          if (newState != DISCOVERED) {
            putParent(parentHm, state, copy);
            if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
          }
          slideNum++;
        }
        // int w = getCarWidth(copy, i, j);
        // while (isMovableTo(copy, i, j - slideNum, LEFT)) {
        //   moveTo(copy, i, copy[i][j - slideNum], LEFT);
        //   slideNum++;
        // }
        // while (slideNum > 0) {
        //   int newState = put(hm, copy, depth);
        //   if (newState != DISCOVERED) {
        //     putParent(parentHm, state, copy);
        //     if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
        //   }
        //   moveTo(copy, i, copy[i][j - slideNum - 1 + w], RIGHT);
        //   slideNum--;
        // }
      }
      if (isMovableTo(state, i, j, DOWN)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i + slideNum, j, DOWN)) {
          moveTo(copy, j, copy[i + slideNum][j], DOWN);
          int newState = put(hm, copy, depth);
          if (newState != DISCOVERED) {
            putParent(parentHm, state, copy);
            if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
          }
          slideNum++;
        }
        // int h = getCarHeight(state, i, j);
        // while (isMovableTo(copy, i + slideNum, j, DOWN)) {
        //   moveTo(copy, j, copy[i + slideNum][j], DOWN);
        //   slideNum++;
        // }
        // while (slideNum > 0) {
        //   int newState = put(hm, copy, depth);
        //   if (newState != DISCOVERED) {
        //     putParent(parentHm, state, copy);
        //     if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
        //   }
        //   moveTo(copy, j, copy[i + slideNum  + 1 - h][j], UP);
        //   slideNum--;
        // }
      }
      if (isMovableTo(state, i, j, UP)) {
        int[][] copy = copyState(state);
        int slideNum = 0;
        while (isMovableTo(copy, i - slideNum, j, UP)) {
          moveTo(copy, j, copy[i - slideNum][j], UP);
          int newState = put(hm, copy, depth);
          if (newState != DISCOVERED) {
            putParent(parentHm, state, copy);
            if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
          }
          slideNum++;
        }
        // int h = getCarHeight(state, i, j);
        // while (isMovableTo(copy, i - slideNum, j, UP)) {
        //   moveTo(copy, j, copy[i - slideNum][j], UP);
        //   slideNum++;
        // }
        // while (slideNum > 0) {
        //   int newState = put(hm, copy, depth);
        //   if (newState != DISCOVERED) {
        //     putParent(parentHm, state, copy);
        //     if (newState == UNDISCOVERED && searchNextState(hm, parentHm, copy, depth + 1)) return true;
        //   }
        //   moveTo(copy, j, copy[i - slideNum  - 1 + h][j], DOWN);
        //   slideNum--;
        // }
      }
    }
  }
  return false;
}

public boolean isFinish(int[][] state, int column, int target) {
  return (state[column][state.length - 1] == target);
}

public boolean nextIsGoal(HashMap hm, HashMap parentHm, int[][] state, int depth) {
  int j = getTargetRow(state);
  if (isMovableTo(state, GOAL_COLUMN, j, RIGHT)) {
    int[][] copy = copyState(state);
    int moveWid = state.length - 1 - j;
    for (int k = 0; k < moveWid; k++) {
      if (! isMovableTo(copy, GOAL_COLUMN, j + k, RIGHT)) return false;
      moveTo(copy, GOAL_COLUMN, copy[GOAL_COLUMN][j + k], RIGHT);
    }
    put(hm, copy, depth);
    putParent(parentHm, state, copy);
    return true;
  }
  return false;
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

public int put(HashMap hm, int[][] state, int depth) {
  String str = convertStateToString(state);
  if (hm.containsKey(str)) {
    if (PApplet.parseInt(hm.get(str).toString()) <= depth) return DISCOVERED;
    hm.remove(str);
    hm.put(str, depth + 1);
    return UPDATE;
  }
  hm.put(str, depth + 1);
  return UNDISCOVERED;
}

public void putParent(HashMap parentHm, int[][] parentState, int[][] newState) {
  String parentStr = convertStateToString(parentState);
  String newStr = convertStateToString(newState);
  if (parentHm.containsKey(newStr)) parentHm.remove(newStr);
  parentHm.put(newStr, parentStr);
}

public int getTargetRow(int[][] state) {
  int row = 0;
  for (int j = 0; j < state.length; j++) {
    row = (state[GOAL_COLUMN][j] == T) ? j : row;
  }
  return row;
}

public int getCarWidth(int[][] state, int i, int j) {
  int r = 0;
  for (int k = 0; k < state.length; k++) {
    if (state[i][k] == state[i][j]) r++;
  }
  return r;
}

public int getCarHeight(int[][] state, int i, int j) {
  int r = 0;
  for (int k = 0; k < state.length; k++) {
    if (state[k][j] == state[i][j]) r++;
  }
  return r;
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

public int getDepth(HashMap<String, Integer> hm) {
  for (Map.Entry me : hm.entrySet()) {
    String str = me.getKey().toString();
    int[][] state = convertStringToState(str);
    if (isFinish(state, GOAL_COLUMN, T)) return PApplet.parseInt(me.getValue().toString()); // int(hm.get(str).toString());
  }
  return 0;
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

public int[][] copyState(int[][] s) {
  int[][] ans = new int[s.length][s[0].length];
  for (int i = 0; i < s.length; i++) {
    for (int j = 0; j < s[i].length; j++) {
      ans[i][j] = s[i][j];
    }
  }
  return ans;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "rush_hour_heuristic" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
