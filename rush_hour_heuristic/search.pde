final int UNDISCOVERED = 0;
final int DISCOVERED = 1;
final int UPDATE = 2;

void setGoal(int[][] state) {
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
int search(HashMap<String, Integer> hm, HashMap<String, String> parentHm, int[][] startState) {
  hm.put(convertStateToString(startState), 0);
  if (searchNextState(hm, parentHm, startState, 0)) return getDepth(hm);
  println("no answer");
  return 0;
}

boolean searchNextState(HashMap hm, HashMap parentHm, int[][] state, int depth) {
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

boolean isFinish(int[][] state, int column, int target) {
  return (state[column][state.length - 1] == target);
}

boolean nextIsGoal(HashMap hm, HashMap parentHm, int[][] state, int depth) {
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

boolean isMovableTo(int[][] state, int i, int j, int direction) {
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

int put(HashMap hm, int[][] state, int depth) {
  String str = convertStateToString(state);
  if (hm.containsKey(str)) {
    if (int(hm.get(str).toString()) <= depth) return DISCOVERED;
    hm.remove(str);
    hm.put(str, depth + 1);
    return UPDATE;
  }
  hm.put(str, depth + 1);
  return UNDISCOVERED;
}

void putParent(HashMap parentHm, int[][] parentState, int[][] newState) {
  String parentStr = convertStateToString(parentState);
  String newStr = convertStateToString(newState);
  if (parentHm.containsKey(newStr)) parentHm.remove(newStr);
  parentHm.put(newStr, parentStr);
}

int getTargetRow(int[][] state) {
  int row = 0;
  for (int j = 0; j < state.length; j++) {
    row = (state[GOAL_COLUMN][j] == T) ? j : row;
  }
  return row;
}

int getCarWidth(int[][] state, int i, int j) {
  int r = 0;
  for (int k = 0; k < state.length; k++) {
    if (state[i][k] == state[i][j]) r++;
  }
  return r;
}

int getCarHeight(int[][] state, int i, int j) {
  int r = 0;
  for (int k = 0; k < state.length; k++) {
    if (state[k][j] == state[i][j]) r++;
  }
  return r;
}

void moveTo(int[][] state, int line, int car, int direction) {
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

int getDepth(HashMap<String, Integer> hm) {
  for (Map.Entry me : hm.entrySet()) {
    String str = me.getKey().toString();
    int[][] state = convertStringToState(str);
    if (isFinish(state, GOAL_COLUMN, T)) return int(me.getValue().toString()); // int(hm.get(str).toString());
  }
  return 0;
}

ArrayList<String> getPath(HashMap<String, Integer> hm, HashMap<String, String> parentHm) {
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

void printState(int[][] state) {
  for (int[] s : state) {
    for (int ss : s) print(ss + ",");
    println();
  }
  println();
}

int[][] copyState(int[][] s) {
  int[][] ans = new int[s.length][s[0].length];
  for (int i = 0; i < s.length; i++) {
    for (int j = 0; j < s[i].length; j++) {
      ans[i][j] = s[i][j];
    }
  }
  return ans;
}
