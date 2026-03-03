package org.imesense.dynamicspawncontrol.core.mixinconfig;

import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.annotations.MixinConfigFile;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class MixinConfigScanner
{
    public static void scanAndRegister(String packageName)
    {
        EarlyLogBuffer.log(LogManager.INFO, "Scanning for mixin configs in package: " + packageName);

        try
        {
            List<Class<?>> configClasses = findClassesWithAnnotation(packageName, MixinConfigFile.class);

            for (Class<?> clazz : configClasses)
            {
                if (BaseMixinConfig.class.isAssignableFrom(clazz))
                {
                    @SuppressWarnings("unchecked")
                    Class<? extends BaseMixinConfig> configClass = (Class<? extends BaseMixinConfig>) clazz;

                    MixinConfigInitializer.registerConfig(configClass);
                    EarlyLogBuffer.log(LogManager.DEBUG, "Found and registered: " + clazz.getSimpleName());
                }
                else
                {
                    EarlyLogBuffer.log(LogManager.WARN, "Class " + clazz.getSimpleName() +
                            " has @MixinConfigFile but does not extend BaseMixinConfig!");
                }
            }

            EarlyLogBuffer.log(LogManager.INFO, "Found " + configClasses.size() + " mixin config classes");
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(LogManager.ERROR, "Failed to scan for mixin configs: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static List<Class<?>> findClassesWithAnnotation(String packageName, Class<? extends java.lang.annotation.Annotation> annotationClass)
            throws IOException, ClassNotFoundException
    {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) classLoader = MixinConfigScanner.class.getClassLoader();

        Enumeration<URL> resources = classLoader.getResources(packagePath);

        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol))
            {
                File directory = new File(resource.getFile());
                findClassesInDirectory(directory, packageName, classes, annotationClass);
            }
            else if ("jar".equals(protocol))
            {
                findClassesInJar(resource, packagePath, packageName, classes, annotationClass);
            }
        }

        return classes;
    }

    private static void findClassesInDirectory(File directory, String packageName, List<Class<?>> classes,
                                               Class<? extends java.lang.annotation.Annotation> annotationClass) throws ClassNotFoundException
    {
        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                findClassesInDirectory(file, packageName + "." + file.getName(), classes, annotationClass);
            }
            else if (file.getName().endsWith(".class"))
            {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(annotationClass))
                {
                    classes.add(clazz);
                }
            }
        }
    }

    private static void findClassesInJar(URL resource, String packagePath, String packageName,
                                         List<Class<?>> classes, Class<? extends java.lang.annotation.Annotation> annotationClass)
            throws IOException, ClassNotFoundException
    {
        String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));

        try (JarFile jarFile = new JarFile(jarPath))
        {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith(packagePath) && entryName.endsWith(".class"))
                {
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(annotationClass))
                    {
                        classes.add(clazz);
                    }
                }
            }
        }
    }
}