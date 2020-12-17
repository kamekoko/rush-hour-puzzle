//car info

int getTargetRow(int[][] state) {
  int row = 0;
  for (int j = 0; j < state.length; j++) {
    row = (state[GOAL_COLUMN][j] == T) ? j : row;
  }
  return row;
}

int getPriorityCarColumn(int[][] state, int car, int row, int direction) {
  int r = 0;
  if (direction == DOWN) {
    for (int i = 0; i < state.length; i++) {
      if (state[i][row] == car) r = i;
    }
  }
  else if (direction == UP) {
    for (int i = state.length - 1; i >= 0; i--) {
      if (state[i][row] == car) r = i;
    }
  }
  return r;
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
