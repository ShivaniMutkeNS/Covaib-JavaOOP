public abstract class Piece {
    protected String color;
    protected String type;
    protected int row;
    protected int col;
    protected boolean hasMoved;
    
    public Piece(String color, String type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.col = col;
        this.hasMoved = false;
    }
    
    public abstract boolean canMove(int fromRow, int fromCol, int toRow, int toCol);
    public abstract String getPieceFeatures();
    
    public String getPieceInfo() {
        return String.format("Piece: %s %s at (%d,%d), Moved: %s", 
                           color, type, row, col, hasMoved ? "Yes" : "No");
    }
}
