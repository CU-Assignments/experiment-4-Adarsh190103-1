import java.util.*;

class Card {
    String symbol;
    String value;

    public Card(String symbol, String value) {
        this.symbol = symbol;
        this.value = value;
    }

    @Override
    public String toString() {
        return value + " of " + symbol;
    }
}

class CardCollection {
    private final Map<String, List<Card>> cardDeck = Collections.synchronizedMap(new HashMap<>());

   
    public synchronized void addCard(String symbol, String value) {
        cardDeck.putIfAbsent(symbol, new ArrayList<>());
        cardDeck.get(symbol).add(new Card(symbol, value));
    }

    
    public void searchCards(String symbol) {
        Thread searchThread = new Thread(() -> {
            synchronized (cardDeck) {
                if (cardDeck.containsKey(symbol)) {
                    System.out.println("Cards in " + symbol + ": " + cardDeck.get(symbol));
                } else {
                    System.out.println("No cards found in " + symbol + ".");
                }
            }
        });

        searchThread.setPriority(Thread.MIN_PRIORITY); 
        searchThread.start();
    }

   
    public synchronized void displayAllCards() {
        if (cardDeck.isEmpty()) {
            System.out.println("No cards in the collection.");
            return;
        }
        System.out.println("All Cards:");
        TreeMap<String, List<Card>> sortedDeck = new TreeMap<>(cardDeck); 
        for (Map.Entry<String, List<Card>> entry : sortedDeck.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

public class CardManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CardCollection collection = new CardCollection();
        HashSet<String> validSymbols = new HashSet<>(Arrays.asList("Hearts", "Spades", "Diamonds", "Clubs"));

      
        collection.addCard("Hearts", "Ace");
        collection.addCard("Hearts", "King");
        collection.addCard("Spades", "Queen");
        collection.addCard("Diamonds", "Jack");
        collection.addCard("Clubs", "10");

        int choice;
        do {
            System.out.println("\nCard Collection System");
            System.out.println("1. Add Card");
            System.out.println("2. Find Cards by Symbol");
            System.out.println("3. Display All Cards");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter Card Symbol (Hearts, Spades, Diamonds, Clubs): ");
                    String symbol = sc.nextLine();
                    if (!validSymbols.contains(symbol)) {
                        System.out.println("Invalid Symbol! Please enter a valid one.");
                        break;
                    }
                    System.out.print("Enter Card Value (e.g., Ace, King, Queen, 10, 2, etc.): ");
                    String value = sc.nextLine();
                    collection.addCard(symbol, value);
                    System.out.println("Card added successfully!");
                    break;

                case 2:
                    System.out.print("Enter Symbol to Search: ");
                    String searchSymbol = sc.nextLine();
                    collection.searchCards(searchSymbol);
                    break;

                case 3:
                    collection.displayAllCards();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);

        sc.close();
    }
}
