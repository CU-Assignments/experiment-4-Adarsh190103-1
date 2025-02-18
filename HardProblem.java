import java.util.HashSet;
import java.util.Random;

class TicketBookingSystem {
    private final HashSet<Integer> bookedSeats = new HashSet<>();
    private final int totalSeats = 10; // Set total available seats

    public synchronized boolean bookSeat(int seatNumber, String customerType) {
        if (seatNumber < 1 || seatNumber > totalSeats) {
            System.out.println(customerType + " trying to book an invalid seat: " + seatNumber);
            return false;
        }

        if (!bookedSeats.contains(seatNumber)) {
            bookedSeats.add(seatNumber);
            System.out.println(customerType + " successfully booked seat: " + seatNumber);
            return true;
        } else {
            System.out.println(customerType + " tried to book seat " + seatNumber + ", but it's already booked.");
            return false;
        }
    }
}

class BookingThread extends Thread {
    private final TicketBookingSystem bookingSystem;
    private final String customerType;
    private final Random random = new Random();

    public BookingThread(TicketBookingSystem bookingSystem, String customerType, int priority) {
        this.bookingSystem = bookingSystem;
        this.customerType = customerType;
        setPriority(priority); // Set thread priority
    }

    @Override
    public void run() {
        int seatNumber = random.nextInt(10) + 1; // Generate random seat number between 1 and 10
        bookingSystem.bookSeat(seatNumber, customerType);
    }
}

public class TicketBookingApp {
    public static void main(String[] args) {
        TicketBookingSystem bookingSystem = new TicketBookingSystem();

        
        BookingThread vip1 = new BookingThread(bookingSystem, "VIP Customer 1", Thread.MAX_PRIORITY);
        BookingThread vip2 = new BookingThread(bookingSystem, "VIP Customer 2", Thread.MAX_PRIORITY);
        BookingThread normal1 = new BookingThread(bookingSystem, "Regular Customer 1", Thread.NORM_PRIORITY);
        BookingThread normal2 = new BookingThread(bookingSystem, "Regular Customer 2", Thread.NORM_PRIORITY);
        BookingThread normal3 = new BookingThread(bookingSystem, "Regular Customer 3", Thread.MIN_PRIORITY);

      
        vip1.start();
        vip2.start();
        normal1.start();
        normal2.start();
        normal3.start();
    }
}
