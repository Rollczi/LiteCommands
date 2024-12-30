package dev.rollczi.example.jda.level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import net.dv8tion.jda.api.entities.Member;

public class LevelService {

    private final HashMap<Long, LevelEntry> levels = new HashMap<>();
    private final TreeMap<Integer, Set<Long>> levelsIndex = new TreeMap<>();

    public void setLevel(long userId, String name, int level) {
        LevelEntry lastLevel = levels.put(userId, new LevelEntry(userId, name, level));

        if (lastLevel != null) {
            Set<Long> users = levelsIndex.get(lastLevel.level());
            users.remove(userId);

            if (users.isEmpty()) {
                levelsIndex.remove(lastLevel.level());
            }
        }

        levelsIndex.computeIfAbsent(level, k -> new HashSet<>())
            .add(userId);
    }

    public LevelEntry getLevel(Member member) {
        return levels.getOrDefault(member.getIdLong(), new LevelEntry(member.getIdLong(), member.getNickname(), 0));
    }

    public List<LevelEntry> getTopLevels(int amount) {
        return levelsIndex.descendingMap().entrySet().stream()
            .flatMap(entry -> entry.getValue().stream())
            .map(levelId -> levels.get(levelId))
            .limit(amount)
            .toList();
    }

}
