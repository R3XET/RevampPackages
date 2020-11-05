package eu.revamp.packages.utils;
/*
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileManager {

    @Getter private static FileManager instance;
    private Plugin plugin;
    private String prefix = "";
    @Getter private boolean log = false;
    private HashMap<Files, File> files = new HashMap<>();
    private List<String> homeFolders = new ArrayList<>();
    private List<CustomFile> customFiles = new ArrayList<>();
    private HashMap<String, String> autoGenerateFiles = new HashMap<>();
    private HashMap<Files, YamlFile> configurations = new HashMap<>();



    public FileManager setup(Plugin plugin) {
        prefix = "[" + plugin.getName() + "] ";
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        files.clear();
        customFiles.clear();
        //Loads all the normal static files.
        for (Files file : Files.values()) {
            File newFile = new File(plugin.getDataFolder(), file.getFileLocation());
            if (log) System.out.println(prefix + "Loading the " + file.getFileName());
            if (!newFile.exists()) {
                try {
                    File serverFile = new File(plugin.getDataFolder(), "/" + file.getFileLocation());
                    InputStream jarFile = getClass().getResourceAsStream("/" + file.getFileJar());
                    copyFile(jarFile, serverFile);
                } catch (Exception e) {
                    if (log) System.out.println(prefix + "Failed to load " + file.getFileName());
                    e.printStackTrace();
                    continue;
                }
            }
            files.put(file, newFile);
            YamlFile yamlFile = new YamlFile(newFile);
            try {
                yamlFile.load();
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
            configurations.put(file, yamlFile);
            if (log) System.out.println(prefix + "Successfully loaded " + file.getFileName());
        }
        //Starts to load all the custom files.
        if (homeFolders.size() > 0) {
            if (log) System.out.println(prefix + "Loading custom files.");
            for (String homeFolder : homeFolders) {
                File homeFile = new File(plugin.getDataFolder(), "/" + homeFolder);
                if (homeFile.exists()) {
                    String[] list = homeFile.list();
                    if (list != null) {
                        for (String name : list) {
                            if (name.endsWith(".yml")) {
                                CustomFile file = new CustomFile(name, homeFolder, plugin);
                                if (file.exists()) {
                                    customFiles.add(file);
                                    if (log) System.out.println(prefix + "Loaded new custom file: " + homeFolder + "/" + name + ".");
                                }
                            }
                        }
                    }

                } else {
                    homeFile.mkdir();
                    if (log) System.out.println(prefix + "The folder " + homeFolder + "/ was not found so it was created.");
                    for (String fileName : autoGenerateFiles.keySet()) {
                        if (autoGenerateFiles.get(fileName).equalsIgnoreCase(homeFolder)) {
                            homeFolder = autoGenerateFiles.get(fileName);
                            try {
                                File serverFile = new File(plugin.getDataFolder(), homeFolder + "/" + fileName);
                                InputStream jarFile = getClass().getResourceAsStream(homeFolder + "/" + fileName);
                                copyFile(jarFile, serverFile);
                                if (fileName.toLowerCase().endsWith(".yml")) {
                                    customFiles.add(new CustomFile(fileName, homeFolder, plugin));
                                }
                                if (log) System.out.println(prefix + "Created new default file: " + homeFolder + "/" + fileName + ".");
                            } catch (Exception e) {
                                if (log) System.out.println(prefix + "Failed to create new default file: " + homeFolder + "/" + fileName + "!");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (log) System.out.println(prefix + "Finished loading custom files.");
        }
        return this;
    }

    public FileManager logInfo(boolean log) {
        this.log = log;
        return this;
    }

    public FileManager registerCustomFilesFolder(String homeFolder) {
        homeFolders.add(homeFolder);
        return this;
    }

    public FileManager unregisterCustomFilesFolder(String homeFolder) {
        homeFolders.remove(homeFolder);
        return this;
    }

    public FileManager registerDefaultGenerateFiles(String fileName, String homeFolder) {
        autoGenerateFiles.put(fileName, homeFolder);
        return this;
    }

    public FileManager unregisterDefaultGenerateFiles(String fileName) {
        autoGenerateFiles.remove(fileName);
        return this;
    }

    public YamlFile getFile(Files file) {
        return configurations.get(file);
    }

    public CustomFile getFile(String name) {
        for (CustomFile file : customFiles) {
            if (file.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return file;
            }
        }
        return null;
    }

    public void saveFile(Files file) {
        try {
            configurations.get(file).saveWithComments();
        } catch (IOException e) {
            System.out.println(prefix + "Could not save " + file.getFileName() + "!");
            e.printStackTrace();
        }
    }

    public void saveFile(String name) {
        CustomFile file = getFile(name);
        if (file != null) {
            try {
                file.getYamlFile().saveWithComments();
                if (log) System.out.println(prefix + "Successfully saved the " + file.getFileName() + ".");
            } catch (Exception e) {
                System.out.println(prefix + "Could not save " + file.getFileName() + "!");
                e.printStackTrace();
            }
        } else {
            if (log) System.out.println(prefix + "The file " + name + ".yml could not be found!");
        }
    }

    public boolean saveFile(CustomFile file) {
        return file.saveFile();
    }

    public void reloadFile(Files file) {
        YamlFile yamlFile = new YamlFile(files.get(file));
        try {
            yamlFile.load();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        configurations.put(file, yamlFile);
    }

    public void reloadFile(String name) {
        CustomFile file = getFile(name);
        if (file != null) {
            try {
                file.yamlFile = new YamlFile(new File(plugin.getDataFolder(), "/" + file.getHomeFolder() + "/" + file.getFileName()));
                try {
                    file.yamlFile.load();
                } catch (InvalidConfigurationException | IOException e) {
                    e.printStackTrace();
                }
                if (log) System.out.println(prefix + "Successfully reload the " + file.getFileName() + ".");
            } catch (Exception e) {
                System.out.println(prefix + "Could not reload the " + file.getFileName() + "!");
                e.printStackTrace();
            }
        } else {
            if (log) System.out.println(prefix + "The file " + name + ".yml could not be found!");
        }
    }

    public boolean reloadFile(CustomFile file) {
        return file.reloadFile();
    }

    private void copyFile(InputStream in, File out) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(out)) {
            byte[] buf = new byte[1024];
            int i;
            while ((i = in.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } finally {
            if (in != null) {
                in.close();
            }

        }
    }

    public enum Files {

        //ENUM_NAME("fileName.yml", "fileLocation.yml"),
        //ENUM_NAME("fileName.yml", "newFileLocation.yml", "oldFileLocation.yml"),
        CONFIG("config.yml", "config.yml");

        @Getter private String fileName;
        @Getter private String fileJar;
        @Getter private String fileLocation;

        Files(String fileName, String fileLocation) {
            this(fileName, fileLocation, fileLocation);
        }

        Files(String fileName, String fileLocation, String fileJar) {
            this.fileName = fileName;
            this.fileLocation = fileLocation;
            this.fileJar = fileJar;
        }

        public YamlFile getFile() {
            return getInstance().getFile(this);
        }

        public void saveFile() {
            getInstance().saveFile(this);
        }

        public void reloadFile() {
            getInstance().reloadFile(this);
        }

    }

    public class CustomFile {

        @Getter private String name;
        @Getter private Plugin plugin;
        @Getter private String fileName;
        @Getter private String homeFolder;
        @Getter private YamlFile yamlFile;

        public CustomFile(String name, String homeFolder, Plugin plugin) {
            this.name = name.replace(".yml", "");
            this.plugin = plugin;
            this.fileName = name;
            this.homeFolder = homeFolder;
            if (new File(plugin.getDataFolder(), "/" + homeFolder).exists()) {
                if (new File(plugin.getDataFolder(), "/" + homeFolder + "/" + name).exists()) {
                    yamlFile = new YamlFile(new File(plugin.getDataFolder(), "/" + homeFolder + "/" + name));
                    try {
                        yamlFile.load();
                    } catch (InvalidConfigurationException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    yamlFile = null;
                }
            } else {
                new File(plugin.getDataFolder(), "/" + homeFolder).mkdir();
                if (log) System.out.println(prefix + "The folder " + homeFolder + "/ was not found so it was created.");
                yamlFile = null;
            }
        }

        public boolean exists() {
            return yamlFile != null;
        }

        public boolean saveFile() {
            if (yamlFile != null) {
                try {
                    yamlFile.saveWithComments();
                    if (log) System.out.println(prefix + "Successfully saved the " + fileName + ".");
                    return true;
                } catch (Exception e) {
                    System.out.println(prefix + "Could not save " + fileName + "!");
                    e.printStackTrace();
                    return false;
                }
            } else {
                if (log) System.out.println(prefix + "There was a null custom file that could not be found!");
            }
            return false;
        }

        public boolean reloadFile() {
            if (yamlFile != null) {
                try {
                    yamlFile = new YamlFile(new File(plugin.getDataFolder(), "/" + homeFolder + "/" + fileName));
                    yamlFile.load();
                    if (log) System.out.println(prefix + "Successfully reload the " + fileName + ".");
                    return true;
                } catch (Exception e) {
                    System.out.println(prefix + "Could not reload the " + fileName + "!");
                    e.printStackTrace();
                }
            } else {
                if (log) System.out.println(prefix + "There was a null custom file that was not found!");
            }
            return false;
        }
    }
}
*/
