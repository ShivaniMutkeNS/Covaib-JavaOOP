class Membership {
    double purchaseAmount;
    Membership(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
    double calculateDiscount() {
        return 0;
    }
}

class SilverMember extends Membership {
    SilverMember(double purchaseAmount) {
        super(purchaseAmount);
    }
    double calculateDiscount() {
        return purchaseAmount * 0.05;
    }
}

class GoldMember extends Membership {
    GoldMember(double purchaseAmount) {
        super(purchaseAmount);
    }
    double calculateDiscount() {
        return purchaseAmount * 0.10;
    }
}

class PlatinumMember extends Membership {
    PlatinumMember(double purchaseAmount) {
        super(purchaseAmount);
    }
    double calculateDiscount() {
        return purchaseAmount * 0.15;
    }
}

public class RetailMembershipTiers {
    public static void main(String[] args) {
        Membership[] members = {
            new SilverMember(1000),
            new GoldMember(2000),
            new PlatinumMember(3000)
        };
        for (Membership member : members) {
            System.out.println(member.getClass().getSimpleName() + ": Discount = " + member.calculateDiscount());
        }
    }
}