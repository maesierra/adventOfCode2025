package net.maesierra.adventOfCode2025.solutions.day11;

import net.maesierra.adventOfCode2025.Runner;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

public class Day11 implements Runner.Solution {

    private static class ServerRack {
        private final Map<String, Set<String>> connectionMap;
        private final Map<Pair<String, String>, Long> cache = new HashMap<>();
        private final Map<CacheKey, Long> cache2 = new HashMap<>();

        record CacheKey(String from, String to, Set<String> visited) {

        }

        public ServerRack(Map<String, Set<String>> connectionMap) {
            this.connectionMap = connectionMap;
        }

        private long findPaths(String from, String to, Set<String> visited, Set<String> using) {
            visited = new HashSet<>(visited);
            visited.add(from);
            Pair<String, String> pair = Pair.of(from, to);
            if (from.equals(to)) {
                //return using.isEmpty() || visited.containsAll(using) ? 1 : 0;
                return 1;
            }
            if (from.equals("out")) {
                return 0;
            }
            if (cache.containsKey(pair)) {
                return cache.get(pair);
            }
            long paths = 0;
            Set<String> destinations = connectionMap.get(from);
            for (var connection: destinations) {
                if (!visited.contains(connection)) {
                    paths += findPaths(connection, to, visited, using);
                }
            }
            cache.put(pair, paths);
            return paths;
        }
        private long findPaths(String from, String to, Set<String> visited) {
            visited = new HashSet<>(visited);
            visited.add(from);
            Pair<String, String> pair = Pair.of(from, to);
            if (from.equals(to)) {
                return 1;
            }
            if (from.equals("out")) {
                return 0;
            }
            if (cache.containsKey(pair)) {
                return cache.get(pair);
            }
            long paths = 0;
            Set<String> destinations = connectionMap.get(from);
            for (var connection: destinations) {
                if (!visited.contains(connection)) {
                    paths += findPaths(connection, to, visited);
                }
            }
            //cache.put(pair, paths);
            return paths;
        }
    }

    @Override
    public String part1(InputStream input, String... params) {
        ServerRack serverRack = parseInput(input);
        return Long.toString(serverRack.findPaths("you", "out", new HashSet<>()));
    }

    @Override
    public String part2(InputStream input, String... params) {
        ServerRack serverRack = parseInput(input);
        return Long.toString(serverRack.findPaths("svr", "out", new HashSet<>(), Set.of("fft", "dac")));
    }

    private static ServerRack parseInput(InputStream input) {
        Map<String, Set<String>> connectionMap = new HashMap<>();
        inputAsStream(input).forEach(s -> {
            String[] parts = s.split(": ");
            Set<String> connection = connectionMap.computeIfAbsent(parts[0], k -> new HashSet<>());
            Stream.of(parts[1].split(" ")).forEach(deviceId -> {
                if (!connectionMap.containsKey(deviceId)) {
                    connectionMap.put(deviceId, new HashSet<>());
                }
                connection.add(deviceId);
            });
        });
        return new ServerRack(connectionMap);
    }

}