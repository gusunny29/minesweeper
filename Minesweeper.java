aimport java.awt.Color;
import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;
import java.util.Random;

//represents constants use for gameplay 
interface Constants {
  //represents the size of a side length of a Tile 
  public int tileSize = 50;
}


//represents the entire game 
class FullGrid extends World {
  //representing the number of rows and columns in this grid 
  int rows; 
  int columns; 

  //representing the 2-D ArrayList of Tiles
  ArrayList<ArrayList<Tile>> grid;

  //represents whether the game is over yet 
  boolean isGameOver; 

  //the random object to help create a random placement 
  //of mines 
  Random rand;

  //constructor for gameplay (all you should need to input is rows and columns)
  FullGrid(int rows, int columns) {
    this.rows = rows; 
    this.columns = columns; 
    this.grid = new ArrayList<ArrayList<Tile>>(); 
    this.rand = new Random(); 
    this.isGameOver = false;

    this.initializeGrid();

  }

  //Constructor for testing purposes 
  FullGrid(ArrayList<ArrayList<Tile>> grid, Random rand, int rows, int columns) {
    this.grid = grid; 
    this.rand = rand; 
    this.rows = rows; 
    this.columns = columns; 
    this.isGameOver = false; 

  }

  //initializes the grid game with mines
  //EFFECT: sets this.grid to be the size of this.rows and this.columns, 
  // changes the neighborlist of each Tile if necessary, and changes the 
  // isMine field of each Tile if necessary 
  void initializeGrid() {

    //Sets the grid initially to an empty nested ArrayList of type Tile 
    this.grid = new ArrayList<ArrayList<Tile>>();

    //initialize the board with each Tile as unflipped and with an empty list of neighbors
    this.initializeBoard(); 

    //mutate this fullGrid's list of neighbors 
    this.addNeighbors();

    //adds Mines to the board equal to half of the number of total Tiles 
    this.addMines();
  }

  //returns the tile in this fullGrid's grid at the given row/column 
  public Tile getTile(int row, int column) {
    //get the Tile at the given row/column in this grid 
    return this.grid.get(row).get(column);
  }


  //initializes the 2-D array with Tiles 
  //EFFECT: initialize the grid with each Tile and with an empty list of neighbors
  void addNeighbors() {

    for (int i = 0; i < this.rows; i++) {

      for (int j = 0; j < this.columns; j++) {

        //if this tile is on the left column of the grid 
        boolean farLeft = (j == 0);
        //if this tile is on the right column of the grid 
        boolean farRight = (j == this.columns - 1);
        //if this tile is on the top row of the grid 
        boolean top = (i == 0);
        //if this tile is on the bottom row of the grid 
        boolean bottom = (i == this.rows - 1);

        //test if this Tile is on the top row of the grid 
        if (!top) {

          //add upperLeft diagonal neighbor 
          if (!farLeft) {
            this.getTile(i, j).addNeighborsToTile(this.getTile(i - 1, j - 1));
          }

          //add the top neighbor
          this.getTile(i, j).addNeighborsToTile(this.getTile(i - 1, j)); 

          //add upperRight diagonal neighbor 
          if (!farRight) {
            this.getTile(i, j).addNeighborsToTile(this.getTile(i - 1, j + 1));
          }
        }

        //test if this Tile is at the on the far left column of the grid 
        if (!farLeft) {
          //need to add left neighbor
          this.getTile(i, j).addNeighborsToTile(this.getTile(i, j - 1));
        }

        //test if this Tile is on the far right column of the grid 
        if (!farRight) {
          //add right neighbor
          this.getTile(i, j).addNeighborsToTile(this.getTile(i, j + 1));
        }


        //test if this Tile is on the bottom row of the grid 
        if (!bottom) {

          //add bottomLeft diagonal neighbor
          if (!farLeft) {
            this.getTile(i, j).addNeighborsToTile(this.getTile(i + 1, j - 1));
          }

          //add the bottom neighbor
          this.getTile(i, j).addNeighborsToTile(this.getTile(i + 1, j));

          //add bottomRight diagonal neighbor 
          if (!farRight) {
            this.getTile(i, j).addNeighborsToTile(this.getTile(i + 1, j + 1));
          }
        }
      }
    }
  }


  //initializes the 2-D array with Tiles 
  //EFFECT: initialize the grid with each Tile as unflipped and with an empty list of neighbors
  void initializeBoard() {

    //create a 2-D ArrayList to avoid mutating original grid until the end 
    ArrayList<ArrayList<Tile>> buildGrid = new ArrayList<ArrayList<Tile>>();

    //constructs the rows of the array 
    for (int i = 0; i < this.rows; i++) {

      //construct an empty ArrayList<Tile>
      ArrayList<Tile> buildRow = new ArrayList<Tile>();

      //constructs the columns of the array 
      for (int j = 0; j < this.columns; j++) {

        //adds the Tile to the row 
        buildRow.add(new Tile(new ArrayList<Tile>()));
      }

      //Adds the row to the overall building grid 
      buildGrid.add(buildRow);
    }

    //Updates the entire board with an empty grid
    this.grid = buildGrid;
  }


  //help to initialize the board by changing some tiles form false 
  //to true for the boolean field isMine 
  //EFFECT: add mines mines to the grid 
  public void addMines() {
    //assign mines to half the Tiles inside the grid 
    int mines = this.rows * this.columns / 4;
    //there are initially zero mines in the 
    int n = 0;

    //check if the number of mines currently is less than
    //the number of mines we need to produce 
    while (n < mines) {

      //get a random index for the row
      int i = this.rand.nextInt(this.rows);
      //get a random index for the column 
      int j = this.rand.nextInt(this.columns);

      //get the tile at these random indices 
      Tile tileCurrent = this.getTile(i, j);

      //check if the tile already has a mine 
      if (!tileCurrent.isMine) {
        //mutate the isMine field to be true
        tileCurrent.isMine = true;
        //increment the mines soFar by 1
        n++;
      }
    }
  }


  //is every single tile that does not have a mine flipped? 
  public boolean didYouBeatGameYet() {
    //check that every tile without a mine is flipped
    //for every row
    for (int i = 0; i < rows; i++) {
      //for ever tile in this row
      for (int j = 0; j < columns; j++) {
        //if the tile does not have a mine
        if (!(this.getTile(i, j).isMine)) {
          //if this tile is flipped
          if (this.getTile(i, j).flipped) {
            //continue looping
            continue;
          }
          else {
            return false; 
          }
        }
      }
    }
    //if you reach this point in the code then every tile without a mine has been flipped 
    return true; 
  }


  //returns the Tile in the ArrayList<ArrayList<Tile>> 
  //based off of the given Posn location of the click
  //(this is assuming the click occurs inside a valid Tile)
  public Tile tileClicked(Posn p) {
    //find the row of the click
    int i = p.y / Constants.tileSize;
    //find the column of the click 
    int j = p.x / Constants.tileSize;

    //return the tile at the given index 
    return this.getTile(i, j);
  }


  //what to do when the mouse is clicked
  //EFFECT: updates the fields of the Tile clicked to be set to flipped
  //if the LeftButton is pushed. If the right button is pushed, set 
  //the flagged field to be opposite what its previous value was.
  //if Left clicked and there are no neighbor mines, then floodfill 
  @Override
  public void onMouseClicked(Posn pos, String buttonName) {
    //if the click occurred within the boundaries of the game 
    boolean leftBound = pos.x >= 0; 
    boolean rightBound = pos.y <= this.columns * Constants.tileSize; 
    boolean highBound = pos.y >= 0; 
    boolean lowBound = pos.y <= this.rows * Constants.tileSize;

    //if the button clicked is right 
    if (buttonName.equals("RightButton")) {
      //check if the click occurred within a tile at all 
      if (leftBound && rightBound && highBound && lowBound) {

        //the click occurred on a tile (find the tile) 
        Tile clicked = this.tileClicked(pos);

        //flip whether or not the tile was flagged 
        clicked.flagged = !clicked.flagged;
      }
    }

    //if the button clicked was left 
    if (buttonName.equals("LeftButton")) { 

      //check if the click occurred within a tile at all 
      if (leftBound && rightBound && highBound && lowBound) {

        //the click occurred on a tile (find the tile) 
        Tile clicked = this.tileClicked(pos);

        //checking if clicked is a mine
        isGameOver = clicked.floodfill();
      }
    }
  }

  @Override 
  //checks if the world has ended 
  //didYouBeatGameYet and isGameOver can be accessed anywhere in class 
  public WorldEnd worldEnds() {
    //check if you beat the game
    if (this.didYouBeatGameYet()) {
      return new WorldEnd(true, this.makeFinalScene(true));
    }

    //check if you lost the game 
    else if (this.isGameOver) {
      return new WorldEnd(true, this.makeFinalScene(false));
    }

    //the game is not over yet
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  //makes the final Scene for the game
  public WorldScene makeFinalScene(boolean win) {

    WorldScene lastScene = new WorldScene(this.rows * Constants.tileSize, 
        this.columns * Constants.tileSize);

    WorldImage congrats = new TextImage("You beat the game!", 16, Color.GREEN);

    WorldImage youLose = new TextImage("You lose. :(", 16, Color.RED);

    //if the game was beat 
    if (win) {
      lastScene.placeImageXY(congrats, this.rows * Constants.tileSize / 2, 
          this.columns * Constants.tileSize / 2);
      return lastScene;
    }

    //if you clicked on a mine
    lastScene.placeImageXY(youLose, this.rows * Constants.tileSize / 2, 
        this.columns * Constants.tileSize / 2);
    return lastScene;
  }

  //Draws each tile in order from row by row 
  public WorldScene makeScene() {

    WorldScene minesweep = new WorldScene(this.rows * Constants.tileSize, 
        this.columns * Constants.tileSize);

    WorldImage drawing = this.drawGrid(); 

    minesweep.placeImageXY(drawing, this.rows * Constants.tileSize / 2, 
        this.columns * Constants.tileSize / 2);

    return minesweep;
  }


  //Draws the complete grid with each tile having their properties 
  public WorldImage drawGrid() {

    WorldImage empty = new EmptyImage();

    for (int i = 0; i < this.rows; i++) {
      WorldImage row = new EmptyImage();
      for (int j = 0; j < this.columns; j++) {
        //get the Tile
        Tile current = this.getTile(i, j);

        //draw the Tile
        WorldImage drawnTile =  current.drawTile();

        //place drawnTile next to it
        row = new BesideImage(row, drawnTile);
      }

      empty = new AboveImage(empty, row);
    }

    return empty; 
  }
}


//Represents an individual tile and its properties 
class Tile  {
  //whether or not this tile has been flipped yet 
  boolean flipped; 
  //whether or not this tile has been tagged as a suspected mine
  boolean flagged; 
  //whether or not the tile contains a mine
  boolean isMine;
  //List of Neighbors 
  ArrayList<Tile> neighborList;

  //represents how many neighbor mines this Tile has 
  int neighborMines;

  // A random number generator used to determine 
  // whether this tile isMine field is true or false 
  Random rand = new Random();

  //Tile constructor (default values for booleans are false)
  Tile(ArrayList<Tile> neighborList) {
    this.flipped = false; 
    this.flagged = false; 
    this.isMine = false;
    this.neighborList = neighborList;
    this.neighborMines = this.numberOfNeighborMines();
  }

  //added Tile Constructor for testing purposes 
  Tile(ArrayList<Tile> neighborList, boolean isMine, boolean flipped,
      boolean flagged, int neighborMines) {
    this.flipped = flipped; 
    this.flagged = flagged; 
    this.isMine = isMine;
    this.neighborList = neighborList;
    this.neighborMines = neighborMines; 
  }

  //to correctly update the neighborList for each Tile 
  //EFFECT: adds the given tile to this tile's list of neighbors 
  void addNeighborsToTile(Tile tile) {
    this.neighborList.add(tile);
  }

  //returns the number of neighbors for this tile 
  public int numberOfNeighbors() {
    return this.neighborList.size();
  }

  //Returns the number of neighbor mines of a tile 
  public int numberOfNeighborMines() {
    //the number of neighborMines so far 
    int soFar = 0;

    //loop through this Tile's neighborList 
    for (Tile current: this.neighborList) {
      if (current.isMine) {
        soFar++;
      }
    }
    return soFar;
  }

  //creates a floodfill effect by flipping this Tile and each Tile 
  //in this Tile's neighborList that have no neighborMines 
  public boolean floodfill() {

    //check if this cell has been flagged
    if (!this.flagged) {
      //flip the tile 
      this.flipped = true; 

      //check if this Tile has no neighbor mines 
      if (this.numberOfNeighborMines() == 0 && !this.isMine) {
        //floodfill t's neighbors 
        this.floodfillNeighbors();
      }
    }

    //return whether or not this tile contains a Mine
    return this.isMine;
  }


  //floodfills the neighbors of this cell
  public void floodfillNeighbors() {
    //for each Tile in this Tile's neighbor list
    for (Tile t : this.neighborList) {

      //&& !t.flooded

      if (!this.flagged && !t.flipped) {
        //flip the tile and floodfill the neighbors 
        t.floodfill();
      }
    }
  }

  //Draws "this" tile 
  public WorldImage drawTile() {
    //Variable to store the number of neighboring mines 
    int numOfNeighMines = this.numberOfNeighborMines();

    //if flipped and is a bomb
    if (this.flipped) {

      //If the flipped tile is a mine
      if (this.isMine) {
        return new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.RED),
            new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
                OutlineMode.OUTLINE, Color.GRAY), Color.BLACK));
      }

      //If Flipped is true and the number of adjacent bombs is larger than 0
      else if (numOfNeighMines > 0) {
        return new OverlayImage(new TextImage(Integer.toString(numOfNeighMines),
            Color.GREEN),
            new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
                OutlineMode.OUTLINE, Color.GRAY), Color.BLACK));
      }

      //Flipped and number of adj is 0 
      else {
        return new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
            OutlineMode.OUTLINE, Color.GRAY), Color.BLACK);
      }
    }

    //If an unflipped 
    else {

      //if the tile gets flagged
      if (this.flagged) {
        return new OverlayImage(new EquilateralTriangleImage(10, OutlineMode.SOLID,
            Color.ORANGE),
            new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
                OutlineMode.SOLID, Color.GRAY), Color.BLACK));
      }


      //If not flagged, should be a regular tile  
      else {
        return new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
            OutlineMode.SOLID, Color.GRAY), Color.BLACK);
      }

    }
  }
}

//represents examples of the game and its pieces 
class ExamplesMineSweeper {

  //examples of Tiles for 2X2 example 
  Tile topLeft; 
  Tile topRight; 
  Tile bottomLeft; 
  Tile bottomRight; 

  //examples of Tiles for 3X3, left -> right, up -> down(first is upperLeft)
  Tile first; 
  Tile second; 
  Tile third; 
  Tile fourth; 
  Tile fifth; 
  Tile sixth; 
  Tile seventh; 
  Tile eighth; 
  Tile ninth; 

  //examples of ArrayList<Tile> for 2X2 example
  ArrayList<Tile> topLeftNeighbors;
  ArrayList<Tile> topRightNeighbors;
  ArrayList<Tile> bottomLeftNeighbors;
  ArrayList<Tile> bottomRightNeighbors;

  //examples of ArrayList<Tile> for 3X3 example
  ArrayList<Tile> firstNeighbors; 
  ArrayList<Tile> secondNeighbors;
  ArrayList<Tile> thirdNeighbors; 
  ArrayList<Tile> fourthNeighbors;
  ArrayList<Tile> fifthNeighbors; 
  ArrayList<Tile> sixthNeighbors;
  ArrayList<Tile> seventhNeighbors; 
  ArrayList<Tile> eighthNeighbors;
  ArrayList<Tile> ninthNeighbors; 


  //examples of <ArrayList<Tile>> for 2X2 example
  ArrayList<ArrayList<Tile>> ConstructedTwo; 

  //examples of <ArrayList<Tile>> for 3X3 example
  ArrayList<ArrayList<Tile>> ConstructedThree; 

  //examples of 2X2 FullGrid
  FullGrid constructedGameTwo; 

  //examples of 3X3 FullGrid
  FullGrid constructedGameThree; 


  //Tile options 
  WorldImage nonFlipped = new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
      OutlineMode.SOLID, Color.GRAY), Color.BLACK);

  WorldImage flaggedTile = 
      new OverlayImage(new EquilateralTriangleImage(10, OutlineMode.SOLID, Color.ORANGE),
          new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
              OutlineMode.SOLID, Color.GRAY), Color.BLACK));

  WorldImage blankFlipped = 
      new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
          OutlineMode.OUTLINE, Color.GRAY), Color.BLACK);

  WorldImage mineFlipped = 
      new OverlayImage(new CircleImage(10, OutlineMode.SOLID, Color.RED),
          new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
              OutlineMode.OUTLINE, Color.GRAY), Color.BLACK));


  //initialize test conditions 
  void initTestConditions() {

    //assign value to each Tile for 2X2(true/false alternating)
    topLeft = new Tile(new ArrayList<Tile>());
    topRight = new Tile(new ArrayList<Tile>());
    bottomLeft = new Tile(new ArrayList<Tile>());
    bottomRight = new Tile(new ArrayList<Tile>());

    //assign value to each Tile for 3X3 (all false for default)
    first = new Tile(new ArrayList<Tile>()); 
    second = new Tile(new ArrayList<Tile>()); 
    third = new Tile(new ArrayList<Tile>()); 
    fourth = new Tile(new ArrayList<Tile>());
    fifth = new Tile(new ArrayList<Tile>()); 
    sixth = new Tile(new ArrayList<Tile>()); 
    seventh = new Tile(new ArrayList<Tile>()); 
    eighth = new Tile(new ArrayList<Tile>());
    ninth = new Tile(new ArrayList<Tile>()); 

    //update their list of neighbors for each tile in 2X2 
    topLeftNeighbors = new ArrayList<Tile>(); 
    topLeftNeighbors.add(topRight);
    topLeftNeighbors.add(bottomLeft);
    topLeftNeighbors.add(bottomRight);

    topRightNeighbors = new ArrayList<Tile>(); 
    topRightNeighbors.add(topLeft);
    topRightNeighbors.add(bottomLeft);
    topRightNeighbors.add(bottomRight);

    bottomLeftNeighbors = new ArrayList<Tile>(); 
    bottomLeftNeighbors.add(topLeft);
    bottomLeftNeighbors.add(topRight);
    bottomLeftNeighbors.add(bottomRight);

    bottomRightNeighbors = new ArrayList<Tile>(); 
    bottomRightNeighbors.add(topLeft);
    bottomRightNeighbors.add(topRight);
    bottomRightNeighbors.add(bottomLeft);

    //update their list of neighbors for each tile in 3X3 
    firstNeighbors = new ArrayList<Tile>(); 
    firstNeighbors.add(second);
    firstNeighbors.add(fourth);
    firstNeighbors.add(fifth);

    secondNeighbors = new ArrayList<Tile>(); 
    secondNeighbors.add(first);
    secondNeighbors.add(third);
    secondNeighbors.add(fourth);
    secondNeighbors.add(fifth);
    secondNeighbors.add(sixth);

    thirdNeighbors = new ArrayList<Tile>(); 
    thirdNeighbors.add(second);
    thirdNeighbors.add(fifth);
    thirdNeighbors.add(sixth);

    fourthNeighbors = new ArrayList<Tile>(); 
    fourthNeighbors.add(first);
    fourthNeighbors.add(second);
    fourthNeighbors.add(fifth);
    fourthNeighbors.add(seventh);
    fourthNeighbors.add(eighth);

    fifthNeighbors = new ArrayList<Tile>(); 
    fifthNeighbors.add(first);
    fifthNeighbors.add(second);
    fifthNeighbors.add(third);
    fifthNeighbors.add(fourth);
    fifthNeighbors.add(sixth);
    fifthNeighbors.add(seventh);
    fifthNeighbors.add(eighth);
    fifthNeighbors.add(ninth);

    sixthNeighbors = new ArrayList<Tile>(); 
    sixthNeighbors.add(second);
    sixthNeighbors.add(third);
    sixthNeighbors.add(fifth);
    sixthNeighbors.add(eighth);
    sixthNeighbors.add(ninth);

    seventhNeighbors = new ArrayList<Tile>(); 
    seventhNeighbors.add(fourth);
    seventhNeighbors.add(fifth);
    seventhNeighbors.add(eighth);

    eighthNeighbors = new ArrayList<Tile>(); 
    eighthNeighbors.add(fourth);
    eighthNeighbors.add(fifth);
    eighthNeighbors.add(sixth);
    eighthNeighbors.add(seventh);
    eighthNeighbors.add(ninth);

    ninthNeighbors = new ArrayList<Tile>(); 
    ninthNeighbors.add(fifth);
    ninthNeighbors.add(sixth);
    ninthNeighbors.add(eighth);

    //initialize each grid to be empty in 2X2
    ConstructedTwo = new ArrayList<ArrayList<Tile>>(); 

    //initialize each grid to be empty in 3X3
    ConstructedThree = new ArrayList<ArrayList<Tile>>(); 

    //give a random seed to the 2X2 FullGrid game 
    constructedGameTwo = new FullGrid(ConstructedTwo, new Random(20), 2, 2);

    //give a random seed to the 3X3 FullGrid game 
    constructedGameThree = new FullGrid(ConstructedThree, new Random(30), 3, 3);

    //construct the empty 2X2 grid to test against
    ArrayList<Tile> topRow = new ArrayList<Tile>(); 

    topRow.add(topLeft);
    topRow.add(topRight);

    //add the two top tiles to the bottom row 
    ArrayList<Tile> bottomRow = new ArrayList<Tile>(); 

    bottomRow.add(bottomLeft);
    bottomRow.add(bottomRight);

    ConstructedTwo.add(topRow);
    ConstructedTwo.add(bottomRow);


    //construct the empty 3X3 grid to test against
    ArrayList<Tile> firstRow = new ArrayList<Tile>(); 

    firstRow.add(first);
    firstRow.add(second);
    firstRow.add(third);

    //add three tiles to the second row 
    ArrayList<Tile> secondRow = new ArrayList<Tile>(); 

    secondRow.add(fourth);
    secondRow.add(fifth);
    secondRow.add(sixth);

    //add three tiles to the last row 
    ArrayList<Tile> thirdRow = new ArrayList<Tile>(); 

    thirdRow.add(seventh);
    thirdRow.add(eighth);
    thirdRow.add(ninth);

    ConstructedThree.add(firstRow);
    ConstructedThree.add(secondRow);
    ConstructedThree.add(thirdRow);
  }


  //test the addMines mutation method 
  void testAddMines(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //mutate each Tile so that none of them have mines
    this.topLeft.isMine = false; 
    this.bottomLeft.isMine = false; 

    t.checkExpect(this.topLeft.isMine, false);
    t.checkExpect(this.topRight.isMine, false);
    t.checkExpect(this.bottomLeft.isMine, false);
    t.checkExpect(this.bottomRight.isMine, false);

    //addMines to each Tile 
    //although the addMines function is theoretically random for gameplay, 
    //the seed will produce the same "random" combination each time 
    //this seed has nothing to do with how the mines are added 
    this.constructedGameTwo.addMines();

    t.checkExpect(this.topLeft.isMine, false);
    t.checkExpect(this.topRight.isMine, false);
    t.checkExpect(this.bottomLeft.isMine, false);
    t.checkExpect(this.bottomRight.isMine, true);

    //test that each Tile has no mine to start 
    t.checkExpect(this.first.isMine, false);
    t.checkExpect(this.second.isMine, false);
    t.checkExpect(this.third.isMine, false);
    t.checkExpect(this.fourth.isMine, false);
    t.checkExpect(this.fifth.isMine, false);
    t.checkExpect(this.sixth.isMine, false);
    t.checkExpect(this.seventh.isMine, false);
    t.checkExpect(this.eighth.isMine, false);
    t.checkExpect(this.ninth.isMine, false);

    //mutate the isMine field for the above Tiles using addMines
    constructedGameThree.addMines();

    //test that the isMine field has changed to true
    //for some of the Tiles
    t.checkExpect(this.first.isMine, false);
    t.checkExpect(this.second.isMine, false);
    t.checkExpect(this.third.isMine, false);
    t.checkExpect(this.fourth.isMine, false);
    t.checkExpect(this.fifth.isMine, false);
    t.checkExpect(this.sixth.isMine, false);
    t.checkExpect(this.seventh.isMine, true);
    t.checkExpect(this.eighth.isMine, true);
    t.checkExpect(this.ninth.isMine, false);
  }

  //test the addNeighborsToTile method
  void testAddNeighborsToTile(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //check that the tiles in 2X2 initially has an empty neighbor list 
    t.checkExpect(this.topLeft.neighborList, new ArrayList<ArrayList<Tile>>());
    t.checkExpect(this.bottomRight.neighborList, new ArrayList<ArrayList<Tile>>());

    //mutate the neighborList by adding neighbors for 2X2
    this.topLeft.addNeighborsToTile(topRight);
    this.topLeft.addNeighborsToTile(bottomLeft);
    this.topLeft.addNeighborsToTile(bottomRight);

    this.bottomRight.addNeighborsToTile(topLeft);
    this.bottomRight.addNeighborsToTile(topRight);
    this.bottomRight.addNeighborsToTile(bottomLeft);


    //check that addNeighborsToTile mutated properly for 2X2
    t.checkExpect(this.topLeft.neighborList, this.topLeftNeighbors);
    t.checkExpect(this.bottomRight.neighborList, this.bottomRightNeighbors);


    //check that the tiles in the 3X3 initially have an empty neighbor list 
    t.checkExpect(this.first.neighborList, new ArrayList<ArrayList<Tile>>());
    t.checkExpect(this.third.neighborList, new ArrayList<ArrayList<Tile>>());
    t.checkExpect(this.fifth.neighborList, new ArrayList<ArrayList<Tile>>());
    t.checkExpect(this.eighth.neighborList, new ArrayList<ArrayList<Tile>>());

    //mutate the neighborList by adding neighbors for 3X3
    this.first.addNeighborsToTile(second);
    this.first.addNeighborsToTile(fourth);
    this.first.addNeighborsToTile(fifth);

    this.third.addNeighborsToTile(second);
    this.third.addNeighborsToTile(fifth);
    this.third.addNeighborsToTile(sixth);

    this.fifth.addNeighborsToTile(first);
    this.fifth.addNeighborsToTile(second);
    this.fifth.addNeighborsToTile(third);
    this.fifth.addNeighborsToTile(fourth);
    this.fifth.addNeighborsToTile(sixth);
    this.fifth.addNeighborsToTile(seventh);
    this.fifth.addNeighborsToTile(eighth);
    this.fifth.addNeighborsToTile(ninth);

    this.eighth.addNeighborsToTile(fourth);
    this.eighth.addNeighborsToTile(fifth);
    this.eighth.addNeighborsToTile(sixth);
    this.eighth.addNeighborsToTile(seventh);
    this.eighth.addNeighborsToTile(ninth);

    //check that addNeighborsToTile mutated properly for 2X2
    t.checkExpect(this.first.neighborList, this.firstNeighbors);
    t.checkExpect(this.third.neighborList, this.thirdNeighbors);
    t.checkExpect(this.fifth.neighborList, this.fifthNeighbors);
    t.checkExpect(this.eighth.neighborList, this.eighthNeighbors);
  }

  //test the numberOfNeighbors method 
  void testNumberOfNeighbors(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //test that initially each Tile has zero neighbors for a 2X2
    t.checkExpect(this.topLeft.numberOfNeighbors(), 0);
    t.checkExpect(this.bottomRight.numberOfNeighbors(), 0);

    //mutate the neighborList by adding neighbors for a 2X2
    this.topLeft.addNeighborsToTile(topRight);
    this.topLeft.addNeighborsToTile(bottomLeft);
    this.topLeft.addNeighborsToTile(bottomRight);

    this.bottomRight.addNeighborsToTile(topLeft);
    this.bottomRight.addNeighborsToTile(topRight);
    this.bottomRight.addNeighborsToTile(bottomLeft);

    //check that the numberOfNeighbors returns the correct # for a 2X2 
    t.checkExpect(this.topLeft.numberOfNeighbors(), 3);
    t.checkExpect(this.bottomRight.numberOfNeighbors(), 3);

    //test that initially each Tile has zero neighbors for a 3X3
    t.checkExpect(this.second.numberOfNeighbors(), 0);
    t.checkExpect(this.fourth.numberOfNeighbors(), 0);
    t.checkExpect(this.fifth.numberOfNeighbors(), 0);
    t.checkExpect(this.ninth.numberOfNeighbors(), 0);

    //mutate the neighborList by adding neighbors for a 2X2
    this.second.addNeighborsToTile(first);
    this.second.addNeighborsToTile(third);
    this.second.addNeighborsToTile(fourth);
    this.second.addNeighborsToTile(fifth);
    this.second.addNeighborsToTile(sixth);

    this.fourth.addNeighborsToTile(first);
    this.fourth.addNeighborsToTile(second);
    this.fourth.addNeighborsToTile(fifth);
    this.fourth.addNeighborsToTile(seventh);
    this.fourth.addNeighborsToTile(eighth);

    this.fifth.addNeighborsToTile(first);
    this.fifth.addNeighborsToTile(second);
    this.fifth.addNeighborsToTile(third);
    this.fifth.addNeighborsToTile(fourth);
    this.fifth.addNeighborsToTile(sixth);
    this.fifth.addNeighborsToTile(seventh);
    this.fifth.addNeighborsToTile(eighth);
    this.fifth.addNeighborsToTile(ninth);

    this.ninth.addNeighborsToTile(fifth);
    this.ninth.addNeighborsToTile(sixth);
    this.ninth.addNeighborsToTile(eighth);

    //check that the numberOfNeighbors returns the correct # for a 3X3 
    t.checkExpect(this.second.numberOfNeighbors(), 5);
    t.checkExpect(this.fourth.numberOfNeighbors(), 5);
    t.checkExpect(this.fifth.numberOfNeighbors(), 8);
    t.checkExpect(this.ninth.numberOfNeighbors(), 3);
  }


  //test the initializeBoard method 
  void testInitializeBoard(Tester t) {
    //initialize the test conditions
    this.initTestConditions();

    //initialize the board for the 2X2
    this.constructedGameTwo.initializeBoard();

    //create an empty 2X2
    ArrayList<ArrayList<Tile>> emptyTwoByTwoGrid = new ArrayList<ArrayList<Tile>>();

    //represents row one of the empty 2X2
    ArrayList<Tile> rowOne = new ArrayList<Tile>();

    //represents the tiles of row one of the empty 2X2
    rowOne.add(new Tile(new ArrayList<Tile>()));
    rowOne.add(new Tile(new ArrayList<Tile>()));

    //represents row two of the empty 2X2
    ArrayList<Tile> rowTwo = new ArrayList<Tile>();

    //represents the tiles of row two of the empty 2X2
    rowTwo.add(new Tile(new ArrayList<Tile>()));
    rowTwo.add(new Tile(new ArrayList<Tile>()));

    //add the two rows to the empty 2X2
    emptyTwoByTwoGrid.add(rowOne);
    emptyTwoByTwoGrid.add(rowTwo);

    //test that the empty grid has been created properly for a game of any size 
    t.checkExpect(this.constructedGameTwo.grid, emptyTwoByTwoGrid);

    //create an empty 3X3
    ArrayList<ArrayList<Tile>> emptyThreeByThreeGrid = new ArrayList<ArrayList<Tile>>();

    //represents row one of the empty 3X3
    ArrayList<Tile> firstRow = new ArrayList<Tile>();

    //represents the tiles of row one of the empty 3X3
    firstRow.add(new Tile(new ArrayList<Tile>()));
    firstRow.add(new Tile(new ArrayList<Tile>()));
    firstRow.add(new Tile(new ArrayList<Tile>()));

    //represents row two of the empty 3X3
    ArrayList<Tile> secondRow = new ArrayList<Tile>();

    //represents the tiles of row two of the empty 3X3
    secondRow.add(new Tile(new ArrayList<Tile>()));
    secondRow.add(new Tile(new ArrayList<Tile>()));
    secondRow.add(new Tile(new ArrayList<Tile>()));

    //represents row third of the empty 3X3
    ArrayList<Tile> thirdRow = new ArrayList<Tile>();

    //represents the tiles of row two of the empty 3X3
    thirdRow.add(new Tile(new ArrayList<Tile>()));
    thirdRow.add(new Tile(new ArrayList<Tile>()));
    thirdRow.add(new Tile(new ArrayList<Tile>()));

    //add the two rows to the empty 3X3
    emptyThreeByThreeGrid.add(firstRow);
    emptyThreeByThreeGrid.add(secondRow);
    emptyThreeByThreeGrid.add(thirdRow);

    //test that the empty grid has been created properly for a game of any size 
    t.checkExpect(this.constructedGameThree.grid, emptyThreeByThreeGrid);
  }

  //test the getTile method 
  void testGetTile(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //check that the getTile method works for 2X2
    t.checkExpect(constructedGameTwo.getTile(0, 0), topLeft);
    t.checkExpect(constructedGameTwo.getTile(0, 1), topRight);
    t.checkExpect(constructedGameTwo.getTile(1, 0), bottomLeft);
    t.checkExpect(constructedGameTwo.getTile(1, 1), bottomRight);

    //check that the getTile method works for 3x3
    t.checkExpect(constructedGameThree.getTile(0, 0), first);
    t.checkExpect(constructedGameThree.getTile(0, 1), second);
    t.checkExpect(constructedGameThree.getTile(0, 2), third);
    t.checkExpect(constructedGameThree.getTile(1, 0), fourth);
    t.checkExpect(constructedGameThree.getTile(1, 1), fifth);
    t.checkExpect(constructedGameThree.getTile(1, 2), sixth);
    t.checkExpect(constructedGameThree.getTile(2, 0), seventh);
    t.checkExpect(constructedGameThree.getTile(2, 1), eighth);
    t.checkExpect(constructedGameThree.getTile(2, 2), ninth);

  }

  //test the addNeighbors method 
  void testAddNeighbors(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //test that the niehborList for each tile in a 2X2 is initially empty 
    t.checkExpect(this.topLeft.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.topRight.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.bottomLeft.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.bottomRight.neighborList, new ArrayList<Tile>());

    //mutate the neighbor list for each of the tiles using the addNeighbors method 
    constructedGameTwo.addNeighbors();

    //test that the neighborList for each tile in the 2X2 has
    //been updated via addNeighbors
    t.checkExpect(this.topLeft.neighborList, this.topLeftNeighbors);
    t.checkExpect(this.topRight.neighborList, this.topRightNeighbors);
    t.checkExpect(this.bottomLeft.neighborList, this.bottomLeftNeighbors);
    t.checkExpect(this.bottomRight.neighborList, this.bottomRightNeighbors);

    //test that the niehborList for each tile in a 3X3 is initially empty 
    t.checkExpect(this.first.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.second.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.third.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.fourth.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.fifth.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.sixth.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.seventh.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.eighth.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.ninth.neighborList, new ArrayList<Tile>());

    //mutate the neighbor list for each of the tiles using the addNeighbors method 
    constructedGameThree.addNeighbors();

    //test that the neighborList for each tile in the 3X3 has
    //been updated via addNeighbors
    t.checkExpect(this.first.neighborList, this.firstNeighbors);
    t.checkExpect(this.second.neighborList, this.secondNeighbors);
    t.checkExpect(this.third.neighborList, this.thirdNeighbors);
    t.checkExpect(this.fourth.neighborList, this.fourthNeighbors);
    t.checkExpect(this.fifth.neighborList, this.fifthNeighbors);
    t.checkExpect(this.sixth.neighborList, this.sixthNeighbors);
    t.checkExpect(this.seventh.neighborList, this.seventhNeighbors);
    t.checkExpect(this.eighth.neighborList, this.eighthNeighbors);
    t.checkExpect(this.ninth.neighborList, this.ninthNeighbors);
  }

  //test the initializeGrid method 
  void testInitializeGrid(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //initializeGrid just calls helpers, so test
    //that each of the helpers works 

    //mutate each Tile so that none of them have mines
    this.topLeft.isMine = false; 
    this.bottomLeft.isMine = false; 

    t.checkExpect(this.topLeft.isMine, false);
    t.checkExpect(this.topRight.isMine, false);
    t.checkExpect(this.bottomLeft.isMine, false);
    t.checkExpect(this.bottomRight.isMine, false);

    //mutate constructedGameTwo using the addMines helper (initializeGrid calls this) 
    this.constructedGameTwo.addMines();

    t.checkExpect(this.topLeft.isMine, false);
    t.checkExpect(this.topRight.isMine, false);
    t.checkExpect(this.bottomLeft.isMine, false);
    t.checkExpect(this.bottomRight.isMine, true);

    //test that the niehborList for each tile in a 2X2 is initially empty 
    t.checkExpect(this.topLeft.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.topRight.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.bottomLeft.neighborList, new ArrayList<Tile>());
    t.checkExpect(this.bottomRight.neighborList, new ArrayList<Tile>());

    //mutate the neighbor list for each of the tiles using the addNeighbors method 
    constructedGameTwo.addNeighbors();

    //test that the neighborList for each tile in the 2X2 has
    //been updated via addNeighbors
    t.checkExpect(this.topLeft.neighborList, this.topLeftNeighbors);
    t.checkExpect(this.topRight.neighborList, this.topRightNeighbors);
    t.checkExpect(this.bottomLeft.neighborList, this.bottomLeftNeighbors);
    t.checkExpect(this.bottomRight.neighborList, this.bottomRightNeighbors);
  }

  //test the numberOfNeighborMines method 
  void testNumberOfNeighborMines(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //test the intial number of mines is 0
    t.checkExpect(this.topLeft.numberOfNeighborMines(), 0);
    t.checkExpect(this.topRight.numberOfNeighborMines(), 0);
    t.checkExpect(this.bottomLeft.numberOfNeighborMines(), 0);
    t.checkExpect(this.bottomRight.numberOfNeighborMines(), 0);

    //add mines to the 2X2
    constructedGameTwo.addMines();

    //add neighbors 
    constructedGameTwo.addNeighbors();

    //test the numberOfNeighborMines method produces nonzero
    //values if this tile is next to another tile with a mine 
    t.checkExpect(this.topLeft.numberOfNeighborMines(), 1);
    t.checkExpect(this.topRight.numberOfNeighborMines(), 1);
    t.checkExpect(this.bottomLeft.numberOfNeighborMines(), 1);
    t.checkExpect(this.bottomRight.numberOfNeighborMines(), 0);


    //test the initial number of mines is 0
    t.checkExpect(this.first.numberOfNeighborMines(), 0);
    t.checkExpect(this.second.numberOfNeighborMines(), 0);
    t.checkExpect(this.third.numberOfNeighborMines(), 0);
    t.checkExpect(this.fourth.numberOfNeighborMines(), 0);
    t.checkExpect(this.fifth.numberOfNeighborMines(), 0);
    t.checkExpect(this.sixth.numberOfNeighborMines(), 0);
    t.checkExpect(this.seventh.numberOfNeighborMines(), 0);
    t.checkExpect(this.eighth.numberOfNeighborMines(), 0);
    t.checkExpect(this.ninth.numberOfNeighborMines(), 0);

    //add mines to the 3X3
    constructedGameThree.addMines();

    //add neighbors 
    constructedGameThree.addNeighbors();

    //test the numberOfNeighborMines method produces nonzero
    //values if this tile is next to another tile with a mine 
    t.checkExpect(this.first.numberOfNeighborMines(), 0);
    t.checkExpect(this.second.numberOfNeighborMines(), 0);
    t.checkExpect(this.third.numberOfNeighborMines(), 0);
    t.checkExpect(this.fourth.numberOfNeighborMines(), 2);
    t.checkExpect(this.fifth.numberOfNeighborMines(), 2);
    t.checkExpect(this.sixth.numberOfNeighborMines(), 1);
    t.checkExpect(this.seventh.numberOfNeighborMines(), 1);
    t.checkExpect(this.eighth.numberOfNeighborMines(), 1);
    t.checkExpect(this.ninth.numberOfNeighborMines(), 1);
  }

  void testDidYouBeat(Tester t) {
    this.initTestConditions();
    this.constructedGameTwo.addMines();
    this.constructedGameTwo.addNeighbors();

    //All tiles have not been flipped yet 
    t.checkExpect(this.constructedGameTwo.didYouBeatGameYet(), false);
    //In the constructed Game Two grid, all but the bottom right mine have mines on them 

    //flipping the top left shouldnt end the game 
    this.topLeft.flipped = true;
    t.checkExpect(this.constructedGameTwo.didYouBeatGameYet(), false);

    //Flipping the top right and top left should end the game as the bottom right is a mine 
    this.topRight.flipped = true;
    this.bottomLeft.flipped = true;
    t.checkExpect(this.constructedGameTwo.didYouBeatGameYet(), true);

    this.initTestConditions();
    this.constructedGameTwo.addNeighbors();
    this.constructedGameTwo.addMines();

    //If the bottom right tile is flipped and you flip the tile over,
    //the game should end immediatedly basically meaning 

    this.bottomRight.flipped = true;

    t.checkExpect(this.constructedGameTwo.didYouBeatGameYet(), false);


  }

  //Test for worldEnds 
  void testWorldEnds(Tester t) {
    this.initTestConditions();
    this.constructedGameTwo.addNeighbors();
    this.constructedGameTwo.addMines();
    //If the Game is beaten, a certain message should appear 
    //indicating you won 
    this.topLeft.flipped = true;
    this.topRight.flipped = true;
    this.bottomLeft.flipped = true;

    t.checkExpect(this.constructedGameTwo.worldEnds(), 
        new WorldEnd(true, this.constructedGameTwo.makeFinalScene(true)));

    this.initTestConditions();
    this.constructedGameTwo.addNeighbors();
    this.constructedGameTwo.addMines();

    WorldScene base = new WorldScene(100, 100);
    base.placeImageXY(new TextImage("You lose. :(", 16, Color.RED), 50, 50);


    //if the player hits a mine, then a different WorldEnds appears 
    this.constructedGameTwo.isGameOver = true;

    t.checkExpect(this.constructedGameTwo.worldEnds(), new WorldEnd(true, base));


    this.initTestConditions();
    this.constructedGameTwo.addNeighbors(); 
    this.constructedGameTwo.addMines(); 
  }

  //Test makeFinalScene 
  void testMakeFinalScene(Tester t) {
    this.initTestConditions();

    WorldScene base = new WorldScene(100, 100);
    base.placeImageXY(new TextImage("You lose. :(", 16, Color.RED), 50, 50);

    WorldScene win = new WorldScene(100, 100);
    win.placeImageXY(new TextImage("You beat the game!", 16, Color.GREEN), 50, 50);
    //If you win the game
    t.checkExpect(this.constructedGameTwo.makeFinalScene(true), win);

    //if you lose the game 
    t.checkExpect(this.constructedGameTwo.makeFinalScene(false), base);
  }

  //Test for floodfill 
  void testFloodFill(Tester t) {
    this.initTestConditions();

    //Without initializing the neighbors and mines for the constructedGameTwo 
    t.checkExpect(this.topLeft.floodfill(), false);
    t.checkExpect(this.topRight.floodfill(), false);
    t.checkExpect(this.bottomLeft.floodfill(), false);
    t.checkExpect(this.bottomRight.floodfill(), false);

    //Returns if the tile is a mine after the mines are added 
    constructedGameTwo.addNeighbors();
    constructedGameTwo.addMines();
    t.checkExpect(this.topLeft.floodfill(), false);
    t.checkExpect(this.topRight.floodfill(), false);
    t.checkExpect(this.bottomLeft.floodfill(), false);
    t.checkExpect(this.bottomRight.floodfill(), true);

    //Without initializing the neighbors and Mines for the 3x3
    t.checkExpect(this.first.floodfill(), false);
    t.checkExpect(this.second.floodfill(), false);
    t.checkExpect(this.third.floodfill(), false);
    t.checkExpect(this.fourth.floodfill(), false);
    t.checkExpect(this.fifth.floodfill(), false);
    t.checkExpect(this.sixth.floodfill(), false);
    t.checkExpect(this.seventh.floodfill(), false);
    t.checkExpect(this.eighth.floodfill(), false);
    t.checkExpect(this.ninth.floodfill(), false);

    //After initializing neighbors and mines 
    constructedGameThree.addNeighbors();
    constructedGameThree.addMines();

    t.checkExpect(this.first.floodfill(), false);
    t.checkExpect(this.second.floodfill(), false);
    t.checkExpect(this.third.floodfill(), false);
    t.checkExpect(this.fourth.floodfill(), false);
    t.checkExpect(this.fifth.floodfill(), false);
    t.checkExpect(this.sixth.floodfill(), false);
    t.checkExpect(this.seventh.floodfill(), true);
    t.checkExpect(this.eighth.floodfill(), true);
    t.checkExpect(this.ninth.floodfill(), false); 

  }

  //Tests for FloodfillNeighbors
  void testFloodFillNeighbors(Tester t) {
    this.initTestConditions();
    constructedGameTwo.addNeighbors();

    this.topLeft.floodfillNeighbors();
    //After floodfilling all tiles should be flipped as there are no mines 
    t.checkExpect(this.topLeft.flipped, true);
    t.checkExpect(this.topRight.flipped, true);
    t.checkExpect(this.bottomLeft.flipped, true);
    t.checkExpect(this.bottomRight.flipped, true);

    this.initTestConditions();
    //After adding mines, only the topLeft should be flipped
    //as it has mines as neighbors 
    constructedGameTwo.initializeGrid();
    this.topLeft.floodfillNeighbors();

    //This means that the three other grids should not be flipped as the tile 
    //selected has a mine as a neighbor 
    t.checkExpect(this.topRight.flipped, false);
    t.checkExpect(this.bottomLeft.flipped, false);
    t.checkExpect(this.bottomRight.flipped,false);

    constructedGameThree.addNeighbors();
    this.first.floodfillNeighbors();

    //After floodfilling all neighbors, every tile on the 3x3 should be flipped
    t.checkExpect(this.first.flipped, true);
    t.checkExpect(this.second.flipped, true);
    t.checkExpect(this.third.flipped, true);
    t.checkExpect(this.fourth.flipped, true);
    t.checkExpect(this.fifth.flipped, true);
    t.checkExpect(this.sixth.flipped, true);
    t.checkExpect(this.seventh.flipped, true);
    t.checkExpect(this.eighth.flipped, true);
    t.checkExpect(this.ninth.flipped, true);

    this.initTestConditions();
    this.constructedGameThree.addNeighbors();
    this.constructedGameThree.addMines();
    this.first.floodfillNeighbors();

    //After floodfilling, this should flip all accompanied tiles that have 0 
    //mine neighbors 

    t.checkExpect(this.second.flipped, true);
    t.checkExpect(this.third.flipped, true);
    t.checkExpect(this.fourth.flipped, true);
    t.checkExpect(this.fifth.flipped, true);
    t.checkExpect(this.sixth.flipped, true);
    t.checkExpect(this.seventh.flipped, false);
    t.checkExpect(this.eighth.flipped, false);
    t.checkExpect(this.ninth.flipped, false);

  }




  //test the drawTile method 
  void testDrawTile(Tester t) {

    this.initTestConditions();

    constructedGameTwo.addNeighbors();

    t.checkExpect(this.topLeft.drawTile(), this.nonFlipped);
    t.checkExpect(this.bottomRight.drawTile(), this.nonFlipped);
    t.checkExpect(this.topRight.drawTile(), this.nonFlipped);
    t.checkExpect(this.bottomLeft.drawTile(), this.nonFlipped);


    //For a 3x3 that have nothing flipped, they all should be nonflipped 
    constructedGameThree.addNeighbors();

    t.checkExpect(this.first.drawTile(), this.nonFlipped);
    t.checkExpect(this.second.drawTile(), this.nonFlipped);
    t.checkExpect(this.third.drawTile(), this.nonFlipped);
    t.checkExpect(this.fourth.drawTile(), this.nonFlipped);
    t.checkExpect(this.fifth.drawTile(), this.nonFlipped);
    t.checkExpect(this.sixth.drawTile(), this.nonFlipped);
    t.checkExpect(this.seventh.drawTile(), this.nonFlipped); 
    t.checkExpect(this.eighth.drawTile(), this.nonFlipped);
    t.checkExpect(this.ninth.drawTile(), this.nonFlipped);

    //Mutating the cells 
    this.first.flagged = true;
    this.second.flipped = true;


    t.checkExpect(this.first.drawTile(), this.flaggedTile);
    //Initially blank because there are no mines nearby
    t.checkExpect(this.second.drawTile(), this.blankFlipped);

    //After mutating a neighbor to have a mine 
    this.third.flipped = true;
    this.third.isMine = true;

    //Second would have a number on top instead of being blank
    t.checkExpect(this.second.drawTile(), 
        new OverlayImage(new TextImage("1", Color.GREEN),
            new FrameImage(new RectangleImage(Constants.tileSize, Constants.tileSize, 
                OutlineMode.OUTLINE, Color.GRAY), Color.BLACK)));
    t.checkExpect(this.third.drawTile(), this.mineFlipped);
  }


  //Testing the basic functionality for the drawGrid
  void testDrawGrid(Tester t) {
    this.initTestConditions(); 
    this.constructedGameTwo.initializeGrid(); 

    //World image for GameTwo
    WorldImage gameTwo = 
        new AboveImage(
            new AboveImage(
                new EmptyImage(),
                new BesideImage(
                    new BesideImage(new EmptyImage(), this.nonFlipped),
                    this.nonFlipped)),
            new BesideImage(
                new BesideImage(new EmptyImage(),
                    this.nonFlipped), this.nonFlipped));

    t.checkExpect(this.constructedGameTwo.drawGrid(), gameTwo);

    //Initialized the gameboard 
    this.constructedGameThree.initializeGrid();

    //World Image example for 3x3
    WorldImage gameThree =
        new AboveImage(
            new AboveImage(
                new AboveImage(
                    new EmptyImage(),
                    new BesideImage(
                        new BesideImage(
                            new BesideImage(new EmptyImage(), this.nonFlipped),
                            this.nonFlipped), this.nonFlipped)),
                new BesideImage(
                    new BesideImage(
                        new BesideImage(new EmptyImage(),this.nonFlipped), 
                        this.nonFlipped), this.nonFlipped)),
            new BesideImage(
                new BesideImage(
                    new BesideImage(new EmptyImage(), this.nonFlipped),
                    this.nonFlipped), this.nonFlipped));



    t.checkExpect(this.constructedGameThree.drawGrid(), gameThree);

  }


  //test the makeScene method 
  void testMakeScene(Tester t) {
    this.initTestConditions();

    constructedGameTwo.addNeighbors();
    WorldScene minesweep = new WorldScene(100, 100);

    minesweep.placeImageXY(new RectangleImage(100, 100, OutlineMode.OUTLINE, Color.WHITE), 
        Constants.tileSize, Constants.tileSize);
    minesweep.placeImageXY(this.constructedGameTwo.drawGrid(), 50, 50);
    //Displays the makeScene for a 2x2 matrix 
    //This will simply construct each tile by tile to form a full grid 
    t.checkExpect(this.constructedGameTwo.makeScene(), 
        minesweep);


    //Mutating the top left object 
    this.topLeft.flagged = true;

    //Depicting a 2x2 with the top left tile flagged 
    WorldImage gameTwo = 
        new AboveImage(
            new AboveImage(
                new EmptyImage(),
                new BesideImage(
                    new BesideImage(new EmptyImage(), this.flaggedTile),
                    this.nonFlipped)),
            new BesideImage(
                new BesideImage(new EmptyImage(),
                    this.nonFlipped), this.nonFlipped));

    WorldScene minesweep2 = new WorldScene(100, 100);
    minesweep2.placeImageXY(new RectangleImage(100, 100, OutlineMode.OUTLINE, Color.WHITE), 
        Constants.tileSize, Constants.tileSize);
    minesweep2.placeImageXY(gameTwo, Constants.tileSize, Constants.tileSize);
    t.checkExpect(this.constructedGameTwo.makeScene(), minesweep2);

    //Mutating the topRight to be flipped 
    this.topRight.flipped = true; 

    WorldImage gameThree = 
        new AboveImage(
            new AboveImage(
                new EmptyImage(),
                new BesideImage(
                    new BesideImage(new EmptyImage(), this.flaggedTile),
                    this.blankFlipped)),
            new BesideImage(
                new BesideImage(new EmptyImage(),
                    this.nonFlipped), this.nonFlipped));

    WorldScene minesweep3 = new WorldScene(100, 100);
    minesweep3.placeImageXY(gameThree, Constants.tileSize, Constants.tileSize);
    t.checkExpect(this.constructedGameTwo.makeScene(), minesweep3);

  }




  //test the tileClicked method 
  void testTileClicked(Tester t) {
    //initialize the test conditions 
    this.initTestConditions();

    //test that clicking at (0, 0) returns the first tile in this grid
    t.checkExpect(this.constructedGameTwo.tileClicked(new Posn(0, 0)), this.topLeft);
    t.checkExpect(this.constructedGameThree.tileClicked(new Posn(0, 0)), this.first);

    //test for clicking at (25, 25)
    t.checkExpect(this.constructedGameTwo.tileClicked(new Posn(25, 25)), this.topLeft);
    t.checkExpect(this.constructedGameThree.tileClicked(new Posn(25, 25)), this.first);

    //test for clicking at (Constants.tileSize, Constants.tileSize)
    t.checkExpect(this.constructedGameTwo.tileClicked(new Posn(Constants.tileSize, 
        Constants.tileSize)), this.bottomRight);
    t.checkExpect(this.constructedGameThree.tileClicked(new Posn(Constants.tileSize, 
        Constants.tileSize)), this.fifth);

    //test for clicking at (75, 75)
    t.checkExpect(this.constructedGameTwo.tileClicked(new Posn(75, 75)), this.bottomRight);
    t.checkExpect(this.constructedGameThree.tileClicked(new Posn(75, 75)), this.fifth);

    //test for clicking at (110, 85)
    t.checkExpect(this.constructedGameThree.tileClicked(new Posn(110, 85)), this.seventh);
  }


  //test the onMouseClicked method 
  void testOnMouseClicked(Tester t) {
    //set the initial conditions 
    this.initTestConditions();

    //set this.topRight.flagged to be true
    this.topRight.flagged = true;
    //set this.bottomLeft.flagged to be true
    this.bottomLeft.flagged = true;

    //test that the initial condition is as expected 
    t.checkExpect(this.topLeft.flagged, false);
    t.checkExpect(this.topLeft.flipped, false);
    t.checkExpect(this.topRight.flagged, true);
    t.checkExpect(this.topRight.flipped, false);
    t.checkExpect(this.bottomLeft.flagged, true);
    t.checkExpect(this.bottomLeft.flipped, false);
    t.checkExpect(this.bottomRight.flagged, false);
    t.checkExpect(this.bottomRight.flipped, false);

    //click outside of the FullGrid(should do nothing)
    this.constructedGameTwo.onMouseClicked(new Posn(-100, -100), "LeftButton");

    //test that clicking outside the entire Grid does nothing
    t.checkExpect(this.topLeft.flagged, false);
    t.checkExpect(this.topLeft.flipped, false);
    t.checkExpect(this.topRight.flagged, true);
    t.checkExpect(this.topRight.flipped, false);
    t.checkExpect(this.bottomLeft.flagged, true);
    t.checkExpect(this.bottomLeft.flipped, false);
    t.checkExpect(this.bottomRight.flagged, false);
    t.checkExpect(this.bottomRight.flipped, false);

    //test the "RightButton" functionality:

    //mutate the conditions using the onMouseClicked method
    this.constructedGameTwo.onMouseClicked(new Posn(0, 0), "RightButton");
    this.constructedGameTwo.onMouseClicked(new Posn(75, 52), "RightButton");
    this.constructedGameTwo.onMouseClicked(new Posn(57, 2), "RightButton");
    this.constructedGameTwo.onMouseClicked(new Posn(2, 63), "RightButton");

    //test that the flagged field for each Tile has been flipped 
    t.checkExpect(this.topLeft.flagged, true);
    t.checkExpect(this.topRight.flagged, false);
    t.checkExpect(this.bottomLeft.flagged, false);
    t.checkExpect(this.bottomRight.flagged, true);

    //reset the test conditions
    this.initTestConditions();

    //test the "LeftButton" functionality

    //mutate the conditions using the onMouseClicked method
    this.constructedGameTwo.onMouseClicked(new Posn(0, 0), "LeftButton");
    this.constructedGameTwo.onMouseClicked(new Posn(75, 52), "LeftButton");
    this.constructedGameTwo.onMouseClicked(new Posn(57, 2), "LeftButton");
    this.constructedGameTwo.onMouseClicked(new Posn(2, 63), "LeftButton");

    //test that the flipped field for each Tile is set to true 
    t.checkExpect(this.topLeft.flipped, true);
    t.checkExpect(this.topRight.flipped, true);
    t.checkExpect(this.bottomLeft.flipped, true);
    t.checkExpect(this.bottomRight.flipped, true);
  }

  //"Test" that runs our game to output the WorldScene
  
  void testGame(Tester t) {
    this.initTestConditions();
    constructedGameThree.addNeighbors();

    WorldCanvas canvas = new WorldCanvas(3 * Constants.tileSize, 3 * Constants.tileSize);
    //game at an intermediate stage (2X2)
    WorldScene scene = this.constructedGameThree.makeScene();

    //3X3 game at the very beginning (commented out, can only 
    //show one game at a time
    //WorldScene scene = this.constructedGameTwo.makeScene();

    canvas.drawScene(scene);
    canvas.show();
  }
   


  
  //actually run the Game
  void testBigBang(Tester t) {
    this.initTestConditions();

    int x = 20;

    int y = 20;

    this.initTestConditions();

    double TICK_RATE = 1.0 / 28.0; //represents seconds per frame 

    FullGrid testGame = new FullGrid(x, y);

    testGame.bigBang(x * Constants.tileSize, y * Constants.tileSize, TICK_RATE);

  }
   
}