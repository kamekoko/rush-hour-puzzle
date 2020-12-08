String convertStateToString(int[][] state) {
  String str = new String();
  for (int i = 0; i < state.length; i++) {
    for (int j = 0; j < state.length; j++) {
      str += (state[i][j] + ",");
    }
    str += "\n";
  }
  return str;
}

int[][] convertStringToState(String str) {
  int[][] state = new int[GAME_SIZE][GAME_SIZE];
  int address = 0;
  String[] column = split(str, "\n");
  for (String s : column) {
    String[] data = split(s, ",");
    for (String ss : data) {
      if (ss.length() <= 0) continue;
      int i = address / GAME_SIZE;
      int j = address % GAME_SIZE;
      state[i][j] = int(ss);
      address++;
    }
  }
  return state;
}
