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

  boolean isFinish() {
    return (state[GOAL_COLUMN][state.length - 1] == T);
  }

  String toString() {
    String str = new String();
    for (int[] s : state) {
      for (int ss : s) str += (ss + ",");
      str += "\n";
    }
    return str + "\n";
  }
}
