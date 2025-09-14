class Room {
    String type;
    Room(String type) {
        this.type = type;
    }
    int getPrice() {
        return 0;
    }
    String getAmenities() {
        return "Basic";
    }
}

class StandardRoom extends Room {
    StandardRoom() {
        super("Standard");
    }
    int getPrice() {
        return 1000;
    }
    String getAmenities() {
        return "Bed, TV";
    }
}

class DeluxeRoom extends Room {
    DeluxeRoom() {
        super("Deluxe");
    }
    int getPrice() {
        return 2000;
    }
    String getAmenities() {
        return "Bed, TV, AC";
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite");
    }
    int getPrice() {
        return 3500;
    }
    String getAmenities() {
        return "Bed, TV, AC, MiniBar";
    }
}

public class HotelRoomBooking {
    public static void main(String[] args) {
        Room[] rooms = { new StandardRoom(), new DeluxeRoom(), new SuiteRoom() };
        for (Room room : rooms) {
            System.out.println(room.type + ": " + room.getPrice() + " - " + room.getAmenities());
        }
    }
}