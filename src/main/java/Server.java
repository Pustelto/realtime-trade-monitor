import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import static com.hazelcast.jet.Util.entry;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toMap;

public class Server {

    public static void main(String[] args) {
        JetInstance jet = Jet.newJetInstance();
        loadSymbols(jet);
    }

    private static void loadSymbols(JetInstance jet) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                TradeProducer.class.getResourceAsStream("/nasdaqlisted.txt"), UTF_8))
        ) {
            IMapJet<String, String> symbols = jet.getMap("symbols");
            Map<String, String> map = reader.lines()
                                                .skip(1)
                                                .map(l -> {
                                                    String[] split = l.split("\\|");
                                                    return entry(split[0], split[1]);
                                                }).collect(toMap(Entry::getKey, Entry::getValue));
            symbols.putAll(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
