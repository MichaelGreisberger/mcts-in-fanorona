package Fanorona.Player;

@FunctionalInterface
public interface Function4<One, Two, Three, Four> {
    public Four apply(One reward, Two visitedCountChild, Three visitecCountParent);
}
