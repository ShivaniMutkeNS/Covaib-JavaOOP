/**
 * Enumeration representing different user roles and their permissions
 */
public enum UserRole {
    GUEST("Guest", 0, new String[]{"read_public"}),
    USER("Regular User", 1, new String[]{"read_public", "read_private", "create_content"}),
    MODERATOR("Moderator", 2, new String[]{"read_public", "read_private", "create_content", "moderate_content", "manage_users"}),
    ADMIN("Administrator", 3, new String[]{"read_public", "read_private", "create_content", "moderate_content", "manage_users", "system_config"}),
    SUPER_ADMIN("Super Administrator", 4, new String[]{"read_public", "read_private", "create_content", "moderate_content", "manage_users", "system_config", "full_access"});
    
    private final String displayName;
    private final int level;
    private final String[] permissions;
    
    UserRole(String displayName, int level, String[] permissions) {
        this.displayName = displayName;
        this.level = level;
        this.permissions = permissions.clone();
    }
    
    public String getDisplayName() { return displayName; }
    public int getLevel() { return level; }
    public String[] getPermissions() { return permissions.clone(); }
    
    public boolean hasPermission(String permission) {
        for (String perm : permissions) {
            if (perm.equals(permission)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canAccess(UserRole requiredRole) {
        return this.level >= requiredRole.level;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Level %d, %d permissions)", 
            displayName, level, permissions.length);
    }
}
