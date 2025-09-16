package composition.ecommerce;

import java.util.*;

/**
 * Recommendation Engine interface for product recommendations
 */
public interface RecommendationEngine {
    List<Product> getRecommendations(ShoppingCart cart);
}

/**
 * Simple Recommendation Engine implementation
 */
class SimpleRecommendationEngine implements RecommendationEngine {
    private final List<Product> availableProducts;
    
    public SimpleRecommendationEngine() {
        this.availableProducts = Arrays.asList(
            new Product("MOUSE001", "Wireless Mouse", 29.99, "Electronics"),
            new Product("MONITOR001", "4K Monitor", 299.99, "Electronics"),
            new Product("DESK001", "Standing Desk", 199.99, "Furniture"),
            new Product("CHAIR001", "Ergonomic Chair", 149.99, "Furniture")
        );
    }
    
    @Override
    public List<Product> getRecommendations(ShoppingCart cart) {
        // Simple recommendation based on categories in cart
        Set<String> cartCategories = new HashSet<>();
        for (CartItem item : cart.getItems().values()) {
            cartCategories.add(item.getProduct().getCategory());
        }
        
        List<Product> recommendations = new ArrayList<>();
        for (Product product : availableProducts) {
            if (cartCategories.contains(product.getCategory()) && 
                !cart.getItems().containsKey(product.getProductId())) {
                recommendations.add(product);
            }
        }
        
        return recommendations.subList(0, Math.min(3, recommendations.size()));
    }
}
