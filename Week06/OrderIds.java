public final class OrderIds {
    private static long nextId = 1000;

    public static long next() {
        return nextId++;
    }
}

