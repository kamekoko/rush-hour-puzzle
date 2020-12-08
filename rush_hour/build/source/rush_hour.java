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
  
  background(0);
  printState(input);
  println("start" + "\n");

  // HashMap<Integer[][], Integer> hm = new HashMap<Integer[][], Integer>();
  //
  HashMap<String, Integer> hm = new HashMap<String, Integer>();
  int depth = 0;
  hm.put(convertStateToString(input), depth);
  // if (searchNextState(hm, input, depth)) ;
  int start = millis();
  while(true) {
    boolean finish = false;
    HashMap<String, Integer> subHm = new HashMap<String, Integer>();
    for (Map.Entry me : hm.entrySet()) {
      if (depth != PApplet.parseInt(me.getValue().toString())) continue;
      String str = "" + me.getKey();
      int[][] newState = convertStringToState(str);
      if (searchNextState(hm, subHm, newState,depth)) {
        finish = true;
        break;
      }
    }
    if (finish) break;
    for (Map.Entry me : subHm.entrySet()) {
      hm.put("" + me.getKey(), PApplet.parseInt(me.getValue().toString()));
    }
    if (subHm.size() <= 0) {
      println("no answer");
      break;
    }
    depth++;
  }
  println("count : " + depth);
  println("time : " + (millis() - start));
  exit();
}

// void draw() {
//
// }
class Board {
  int[][] state;
  int depth;

  Board(int[][] data) {
    state = data;
    depth = 0;
  }

  public void printState() {
    for (int[] s : this.state) {
      for (int ss : s) print(ss + ",");
      println();
    }
  }

  public void drawBoard() {
    pushMatrix();
    pushStyle();

    translate(100, 100);
    fill(200, 200, 200);

    for (int i = 0; i < state.length; i++) {
      for (int j = 0; j < state.length; j++) {
        rect(BLOCK_SIZE * i, BLOCK_SIZE * j, BLOCK_SIZE, BLOCK_SIZE);
      }
    }

    // draw block
    for (int i = 0; i < state.length; i++) {
      for (int j = 0; j < state.length; j++) {

      }
    }

    popStyle();
    popMatrix();
  }
}
// Game
final int GAME_SIZE = 6;

final int GOAL_COLUMN = 2;
final int T = 6;
int[][] input =
  {{0, 0, 0, 1, 0, 0},
   {2, 0, 0, 1, 3, 3},
   {2, T, T, 0, 4, 0},
   {2, 0, 0, 0, 4, 0},
   {0, 0, 0, 0, 4, 0},
   {0, 0, 0, 0, 5, 5}};

Board board;


// Board
final int BLOCK_SIZE = 200;
final int EMPTY = 0;

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
// void search(HashMap hm) {
//   int depth = 0;
//
//   while(true) {
//     boolean finish = false;
//     for (Map.Entry me : hm.entrySet()) {
//       if (depth != int(me.getValue().toString())) continue;
//       String str = "" + me.getKey();
//       int[][] newState = convertStringToState(str);
//       if (searchNextState(hm, newState,depth)) {
//         finish = true;
//         break;
//       }
//     }
//     if (finish) break;
//     depth++;
//   }
// }

public boolean searchNextState(HashMap hm, HashMap subHm, int[][] state, int depth) { //Hash h
  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      if (state[i][j] == 0) continue;
      if (isMovableTo(state, i, j, RIGHT)) {
        int[][] copy = copyState(state);
        moveTo(copy, i, state[i][j], RIGHT);
        String str = convertStateToString(copy);
        if (! hm.containsKey(str) && ! subHm.containsKey(str)) {
          subHm.put(str, depth + 1);
          if (isFinish(copy, GOAL_COLUMN, T)) return true;
        }
      }
      if (isMovableTo(state, i, j, LEFT)) {
        int[][] copy = copyState(state);
        moveTo(copy, i, state[i][j], LEFT);
        String str = convertStateToString(copy);
        if (! hm.containsKey(str) && ! subHm.containsKey(str)) {
          subHm.put(str, depth + 1);
          if (isFinish(copy, GOAL_COLUMN, T)) return true;
        }
      }
      if (isMovableTo(state, i, j, DOWN)) {
        int[][] copy = copyState(state);
        moveTo(copy, j, state[i][j], DOWN);
        String str = convertStateToString(copy);
        if (! hm.containsKey(str) && ! subHm.containsKey(str)) {
          subHm.put(str, depth + 1);
          if (isFinish(copy, GOAL_COLUMN, T)) return true;
        }
      }
      if (isMovableTo(state, i, j, UP)) {
        int[][] copy = copyState(state);
        moveTo(copy, j, state[i][j], UP);
        String str = convertStateToString(copy);
        if (! hm.containsKey(str) && ! subHm.containsKey(str)) {
          subHm.put(str, depth + 1);
          if (isFinish(copy, GOAL_COLUMN, T)) return true;
        }
      }
    }
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

public boolean isFinish(int[][] state, int column, int target) {
  if (state[column][state.length - 1] == target) printState(state);
  return (state[column][state.length - 1] == target);
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

public void printState(int[][] state) {
  for (int[] s : state) {
    for (int ss : s) print(ss + ",");
    println();
  }
  println();
}
  public void settings() {  size(1500, 1500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "rush_hour" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
