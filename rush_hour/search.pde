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

boolean searchNextState(HashMap hm, HashMap subHm, int[][] state, int depth) { //Hash h
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

boolean isFinish(int[][] state, int column, int target) {
  if (state[column][state.length - 1] == target) printState(state);
  return (state[column][state.length - 1] == target);
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

void printState(int[][] state) {
  for (int[] s : state) {
    for (int ss : s) print(ss + ",");
    println();
  }
  println();
}
