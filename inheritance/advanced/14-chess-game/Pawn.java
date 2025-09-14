public class Pawn extends Piece {
    public Pawn(String color, int row, int col) {
        super(color, "Pawn", row, col);
    }
    
    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);
        
        if (color.equals("White")) {
            return (rowDiff == 1 && colDiff == 0) || 
                   (rowDiff == 2 && colDiff == 0 && !hasMoved);
        } else {
            return (rowDiff == -1 && colDiff == 0) || 
                   (rowDiff == -2 && colDiff == 0 && !hasMoved);
        }
    }
    
    @Override
    public String getPieceFeatures() {
        return "Pawn Features: Moves forward 1-2 squares, Captures diagonally, Can promote";
    }
}
