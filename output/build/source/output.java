import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class output extends PApplet {

public void setup() {
  String savefile = "results.csv";
  PrintWriter output = createWriter(savefile);
  output.println("input,bfs-depth,dfs-depth,bfs-time,dfs-time");

  for (int k = 1; k < 21; k++) {
    // input
    String filename = "board" + k;
    String bfsfile = filename + "-bfs.txt";
    String dfsfile = filename + "-dfs.txt";
    String[] bfstxt = loadStrings(bfsfile);
    String[] dfstxt = loadStrings(dfsfile);

    output.print(filename + ",");

    int bfsDepth = PApplet.parseInt(bfstxt[0].split(" : ", 0)[1]);
    int dfsDepth = PApplet.parseInt(dfstxt[0].split(" : ", 0)[1]);
    int bfsTime = PApplet.parseInt(bfstxt[2].split(" : ", 0)[1].split("ms",0)[0]);
    int dfsTime = PApplet.parseInt(dfstxt[2].split(" : ", 0)[1].split("ms",0)[0]);

    output.println(bfsDepth + "," + dfsDepth + "," + bfsTime + "," + dfsTime);
  }

  output.flush();
  output.close();
  exit();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "output" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
