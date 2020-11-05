package eu.revamp.packages.managers;

public interface PluginHook<T> {

    T setup();

    String getName();


}