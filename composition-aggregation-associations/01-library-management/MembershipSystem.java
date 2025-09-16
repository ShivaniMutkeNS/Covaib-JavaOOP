package composition.library;

/**
 * Membership System interface for Strategy Pattern
 */
public interface MembershipSystem {
    boolean isValidMember(String memberId);
    void recordBorrow(String memberId, String isbn);
    void recordReturn(String memberId, String isbn);
    int getTotalMembers();
    String getType();
}
