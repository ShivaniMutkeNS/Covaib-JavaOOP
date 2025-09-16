package composition.library;

import java.util.*;

/**
 * Basic Membership System Implementation
 */
public class BasicMembershipSystem implements MembershipSystem {
    private final Set<String> validMembers;
    private final Map<String, List<String>> borrowHistory;
    
    public BasicMembershipSystem() {
        this.validMembers = new HashSet<>();
        this.borrowHistory = new HashMap<>();
        
        // Initialize with some members
        validMembers.add("M001");
        validMembers.add("M002");
        validMembers.add("M003");
    }
    
    @Override
    public boolean isValidMember(String memberId) {
        return validMembers.contains(memberId);
    }
    
    @Override
    public void recordBorrow(String memberId, String isbn) {
        borrowHistory.computeIfAbsent(memberId, k -> new ArrayList<>()).add(isbn);
    }
    
    @Override
    public void recordReturn(String memberId, String isbn) {
        List<String> history = borrowHistory.get(memberId);
        if (history != null) {
            history.remove(isbn);
        }
    }
    
    @Override
    public int getTotalMembers() {
        return validMembers.size();
    }
    
    @Override
    public String getType() {
        return "Basic Membership System";
    }
}
