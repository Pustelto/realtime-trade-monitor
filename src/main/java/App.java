import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.query.impl.predicates.EqualPredicate;
import model.Trade;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws InterruptedException {
        JetInstance jet = Jet.newJetClient();

        while (true) {
            IMapJet<String, Long> results = jet.getMap("query1_Results");
            IMapJet<String, Trade> trades = jet.getMap("trades");
            long count = 0;
            long start = 0;
            while (true) {
                long prevCount = count;
                count = trades.size();
                long end = System.nanoTime();
                System.out.printf("Total trades ingested so far: %,d%n", count);
                if (start > 0) {
                    long elapsed = TimeUnit.NANOSECONDS.toSeconds(end - start);
                    long recordCount = count - prevCount;
                    System.out.println("Processed " + recordCount/(double)elapsed + " recs/s");
                }
                start = end;
                Thread.sleep(5000);
            }
        }
    }

    private static void queryUsingIndex(IMapJet<String, Trade> trades) {
        long begin = System.nanoTime();
        Collection<Trade> matches = trades.values(new EqualPredicate("symbol", "AAPL"));
        long elapsed = System.nanoTime() - begin;
        System.out.println("Query took " + TimeUnit.NANOSECONDS.toMillis(elapsed) + "ms");
        printTrades(matches);
    }

    private static void printTrades(Collection<Trade> trades) {
        if (trades != null) {
            trades.stream().limit(5).forEach(System.out::println);
        }
    }
}
