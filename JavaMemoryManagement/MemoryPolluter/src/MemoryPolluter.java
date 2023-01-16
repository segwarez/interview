public class MemoryPolluter {
    static final int GIGABYTE = 1024 * 1024 * 1024;
    static final int ITERATION_COUNT = 1024 * 10;

    public static void main(String[] args) {
        System.out.println("Start polluting");
        for (int i = 0; i < ITERATION_COUNT; i++) {
            byte[] array = new byte[GIGABYTE];
        }
        System.out.println("Terminating");
    }
}
