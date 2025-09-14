public class ChessDemo {
    public static void main(String[] args) {
        System.out.println("♟️ CHESS GAME PIECES ♟️");
        System.out.println("=" .repeat(50));
        
        Piece[] pieces = {
            new Pawn("White", 1, 0),
            new Pawn("Black", 6, 0)
        };
        
        System.out.println("\n📋 PIECE INFORMATION:");
        for (Piece piece : pieces) {
            System.out.println(piece.getPieceInfo());
        }
        
        System.out.println("\n🎯 MOVE VALIDATION:");
        for (Piece piece : pieces) {
            System.out.println(piece.getPieceFeatures());
            System.out.println("Can move from (1,0) to (2,0): " + piece.canMove(1, 0, 2, 0));
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}
