package composition.ecommerce;

import java.util.*;

/**
 * Wishlist Manager for managing customer wishlists
 */
public class WishlistManager {
    private final Map<String, Set<String>> customerWishlists;
    
    public WishlistManager() {
        this.customerWishlists = new HashMap<>();
    }
    
    public void addToWishlist(String customerId, Product product) {
        customerWishlists.computeIfAbsent(customerId, k -> new HashSet<>())
                        .add(product.getProductId());
        System.out.println("❤️ Added to wishlist: " + product.getName());
    }
    
    public void removeFromWishlist(String customerId, String productId) {
        Set<String> wishlist = customerWishlists.get(customerId);
        if (wishlist != null) {
            wishlist.remove(productId);
            System.out.println("❤️ Removed from wishlist: " + productId);
        }
    }
    
    public Set<String> getWishlist(String customerId) {
        return customerWishlists.getOrDefault(customerId, new HashSet<>());
    }
}
