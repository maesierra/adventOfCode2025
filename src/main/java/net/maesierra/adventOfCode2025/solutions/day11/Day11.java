package net.maesierra.adventOfCode2025.solutions.day11;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;

public class Day11 implements Runner.Solution {

    private static class ServerRack {
        private final Map<String, Set<String>> connectionMap;
        private final Map<CacheKey, FindPathsResult> cache = new HashMap<>();

        record CacheKey(String from, String to) {

        }

        record FindPathsResult(long valid, long dac, long fft, long none) {

            long all() {
                return valid + dac + fft + none;
            }
        }


        public ServerRack(Map<String, Set<String>> connectionMap) {
            this.connectionMap = connectionMap;
        }

        CacheKey cacheKey(String from, String to) {
            return new CacheKey(from, to);
        }


        private FindPathsResult findPaths(String from, String to, Set<String> visited) {
            visited = new HashSet<>(visited);
            visited.add(from);
            CacheKey cacheKey = cacheKey(from, to);
            boolean dac = from.equals("dac");
            boolean fft = from.equals("fft");
            if (from.equals(to)) {
                return new FindPathsResult(0, 0, 0, 1);
            }
            if (cache.containsKey(cacheKey)) {
                debug("Cache used for %s -> %s".formatted(from, to));
                return cache.get(cacheKey);
            }
            long validPaths = 0;
            long dacPaths = 0;
            long fftPaths = 0;
            long nonePaths = 0;
            Set<String> destinations = connectionMap.get(from);
            for (var connection : destinations) {
                if (!visited.contains(connection)) {
                    FindPathsResult res = findPaths(connection, to, visited);
                    validPaths += res.valid;
                    if (dac) {
                        validPaths += res.fft; //All the fft paths become valid
                        dacPaths += res.dac; //dac paths stay the same
                        dacPaths += res.none; //none path become dac
                    } else if (fft) {
                        validPaths += res.dac; //All the dac paths become valid
                        fftPaths += res.fft;
                        fftPaths += res.none;
                    } else {
                        dacPaths += res.dac;
                        fftPaths += res.fft;
                        nonePaths += res.none;
                    }
                }
            }
            FindPathsResult result = new FindPathsResult(validPaths, dacPaths, fftPaths, nonePaths);
            cache.put(cacheKey, result);
            return result;
        }
    }

    @Override
    public String part1(InputStream input, String... params) {
        ServerRack serverRack = parseInput(input);
        var result = serverRack.findPaths("you", "out", Set.of());
        return Long.toString(result.all());
    }

    @Override
    public String part2(InputStream input, String... params) {
        ServerRack serverRack = parseInput(input);
        var result = serverRack.findPaths("svr", "out", Set.of());
        Logger.info(result.toString());
        return Long.toString(result.valid());
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