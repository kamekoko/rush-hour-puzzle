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

int search(HashMap<String, Integer> hm, HashMap<String, String> parentHm, int[][] startState) {
  hm.put(convertStateToString(startState), 0);
  int[][] state = copyState(startState);

  while (true) {
    int depth = int(hm.get(convertStateToString(state)).toString());
    if (isFinish(hm, parentHm, state, depth)) return int(hm.get(convertStateToString(state)).toString()) + 1;
    int[][] newState = nextState(hm, parentHm, state, depth);
    if (newState == null) {
      if (! parentHm.containsKey(convertStateToString(state))) break;
      state = convertStringToState(parentHm.get(convertStateToString(state)).toString());
    }
    else state = newState;
  }

  println("no answer");
  return 0;
}

int[][] nextState(HashMap hm, HashMap parentHm, int[][] state, int depth) {

  // priority
  int priorityCarsRowBit = 0;
  int priorityCarsBit = 0;
  setPriority(state, priorityCarsRowBit, priorityCarsBit);

  for (int j = 0; j < state.length; j++) {
    if ((priorityCarsRowBit & 1<<j) == 0) continue;

    int column = getPriorityCarColumn(state, state[GOAL_COLUMN][j], j, DOWN);
    if (isMovableTo(state, column, j, DOWN)) {
      int slideNum = 0;
      int[][] newState = copyState(state);
      while (isMovableTo(newState, column + slideNum, j, DOWN)) {
        moveTo(newState, j, newState[column + slideNum][j], DOWN);
        if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
        slideNum++;
      }
    }

    column = getPriorityCarColumn(state, state[GOAL_COLUMN][j], j, UP);
    if (isMovableTo(state, column, j, UP)) {
      int slideNum = 0;
      int[][] newState = copyState(state);
      while (isMovableTo(newState, column - slideNum, j, UP)) {
        moveTo(newState, j, newState[column - slideNum][j], UP);
        if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
        slideNum++;
      }
    }
  }

  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      if (state[i][j] == 0) continue;
      if ((priorityCarsBit & 1 << state[i][j]) != 0) continue;

      if (isMovableTo(state, i, j, RIGHT)) {
        int slideNum = 0;
        int[][] newState = copyState(state);
        while (isMovableTo(newState, i, j + slideNum, RIGHT)) {
          moveTo(newState, i, newState[i][j + slideNum], RIGHT);
          if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
          slideNum++;
        }
      }

      if (isMovableTo(state, i, j, LEFT)) {
        int slideNum = 0;
        int[][] newState = copyState(state);
        while (isMovableTo(newState, i, j - slideNum, LEFT)) {
          moveTo(newState, i, newState[i][j - slideNum], LEFT);
          if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
          slideNum++;
        }
      }

      if (isMovableTo(state, i, j, DOWN)) {
        int slideNum = 0;
        int[][] newState = copyState(state);
        while (isMovableTo(newState, i + slideNum, j, DOWN)) {
          moveTo(newState, j, newState[i + slideNum][j], DOWN);
          if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
          slideNum++;
        }
      }

      if (isMovableTo(state, i, j, UP)) {
        int slideNum = 0;
        int[][] newState = copyState(state);
        while (isMovableTo(newState, i - slideNum, j, UP)) {
          moveTo(newState, j, newState[i - slideNum][j], UP);
          if (setHash(hm, parentHm, state, newState, depth) == UNDISCOVERED) return newState;
          slideNum++;
        }
      }
    }
  }
  return null;
}

boolean isFinish(HashMap hm, HashMap parentHm, int[][] state, int depth) {
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

int setHash(HashMap hm, HashMap parentHm, int[][] state, int[][] newState, int depth) {
  int r = put(hm, newState, depth);
  if (r != DISCOVERED) putParent(parentHm, state, newState);
  return r;
}

int put(HashMap hm, int[][] state, int depth) {
  String str = convertStateToString(state);
  if (hm.containsKey(str)) {
    if (int(hm.get(str).toString()) <= depth + 1) return DISCOVERED;
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

void setPriority(int[][] state, int priorityCarsRowBit, int priorityCarsBit) {
  int tRow = getTargetRow(state);
  for (int j = tRow + 1; j < state.length; j++) {
    int index = state[GOAL_COLUMN][j];
    if (index == 0) continue;
    priorityCarsRowBit |= 1 << j;
    priorityCarsBit |= 1 << index;
  }
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
    if (state[GOAL_COLUMN][state.length - 1] == T) return int(me.getValue().toString()); // int(hm.get(str).toString());
  }
  return 0;
}

ArrayList<String> getPath(HashMap<String, Integer> hm, HashMap<String, String> parentHm) {
  ArrayList<String> path = new ArrayList<String>();
  for (Map.Entry me : hm.entrySet()) {
    String str = me.getKey().toString();
    int[][] state = convertStringToState(str);
    if (state[GOAL_COLUMN][state.length - 1] == T) {
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
