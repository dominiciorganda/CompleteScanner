public class Product {
    String name;
    int cost;

    public Product(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public static void main(String[] args) {
        int pricelimit = Integer.parseInt(args[0]);
        System.out.println("Products below " + pricelimit);
        Product[] prodlist = new Product[3];
        prodlist[0] = new Product("potato", 5);
        prodlist[1] = new Product("wine", 47);
        prodlist[2] = new Product("honey", 22);
        for (Product prod : prodlist) {
            int price = prod.cost * 120 / 100;
            if (price < pricelimit) {
                System.out.println(prod.name);
            }
        }
    }
}
