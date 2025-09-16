package composition.library;

import java.util.*;

/**
 * Premium Membership System with enhanced features
 */
public class PremiumMembershipSystem implements MembershipSystem {
    private final Map<String, Member> members;
    private final Map<String, List<String>> borrowHistory;
    
    public PremiumMembershipSystem() {
        this.members = new HashMap<>();
        this.borrowHistory = new HashMap<>();
        
        // Initialize with premium members
        members.put("P001", new Member("P001", "Premium", 10));
        members.put("P002", new Member("P002", "Premium", 15));
        members.put("P003", new Member("P003", "VIP", 20));
    }
    
    @Override
    public boolean isValidMember(String memberId) {
        return members.containsKey(memberId);
    }
    
    @Override
    public void recordBorrow(String memberId, String isbn) {
        Member member = members.get(memberId);
        if (member != null && member.canBorrow()) {
            borrowHistory.computeIfAbsent(memberId, k -> new ArrayList<>()).add(isbn);
            member.incrementBorrowCount();
            System.out.println("Premium member " + memberId + " borrowed book. Remaining limit: " + 
                             (member.getBorrowLimit() - member.getCurrentBorrows()));
        }
    }
    
    @Override
    public void recordReturn(String memberId, String isbn) {
        List<String> history = borrowHistory.get(memberId);
        if (history != null && history.remove(isbn)) {
            Member member = members.get(memberId);
            if (member != null) {
                member.decrementBorrowCount();
            }
        }
    }
    
    @Override
    public int getTotalMembers() {
        return members.size();
    }
    
    @Override
    public String getType() {
        return "Premium Membership System";
    }
    
    private static class Member {
        private final String id;
        private final String tier;
        private final int borrowLimit;
        private int currentBorrows;
        
        public Member(String id, String tier, int borrowLimit) {
            this.id = id;
            this.tier = tier;
            this.borrowLimit = borrowLimit;
            this.currentBorrows = 0;
        }
        
        public boolean canBorrow() {
            return currentBorrows < borrowLimit;
        }
        
        public void incrementBorrowCount() { currentBorrows++; }
        public void decrementBorrowCount() { currentBorrows = Math.max(0, currentBorrows - 1); }
        public int getBorrowLimit() { return borrowLimit; }
        public int getCurrentBorrows() { return currentBorrows; }
    }
}
