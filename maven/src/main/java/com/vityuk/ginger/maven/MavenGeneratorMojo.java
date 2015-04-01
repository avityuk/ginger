/*
 * Copyright 2013 Andriy Vityuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vityuk.ginger.maven;

import com.vityuk.ginger.generator.InterfaceGenerator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Mojo(name = "ginger-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class MavenGeneratorMojo extends AbstractMojo {

    /**
     * The output directory of modified classes
     */
    @Parameter(property = "ginger.outputDirectory", defaultValue = "${project.build.directory}/generated-sources/ginger")
    String outputDirectory;

    /**
     * The working directory
     */
    @Parameter(property = "ginger.workingDirectory", defaultValue = "${project.basedir}/src/main/resources")
    String workingDirectory;

    /**
     * The properties files to parse
     */
    @Parameter(property = "ginger.includeFiles", required = true)
    String[] includeFiles;

    /**
     * The return type of the return object
     */
    @Parameter(property = "ginger.returnType", defaultValue = "java.lang.String")
    String returnType;

    /**
     * The return type of the interfaces
     */
    @Parameter(property = "ginger.localizableType", defaultValue = "com.vityuk.ginger.Localizable")
    String localizableType;

    /**
     * The return type of the key
     */
    @Parameter(property = "ginger.localizableType", defaultValue = "com.vityuk.ginger.Localizable$Key")
    String keyType;

    /**
     * The return type of the key
     */
    @Parameter(property = "ginger.pluralCountType", defaultValue = "com.vityuk.ginger.PluralCount")
    String pluralCountType;

    /**
     * Skip this plugin
     */
    @Parameter(property = "ginger.skip", defaultValue = "false")
    private boolean skip;

    /**
     * Add to the the test classpath to the plugin classpath
     */
    @Parameter(property = "ginger.testClasspath", defaultValue = "false")
    private boolean testClasspath;

    /**
     * The associated maven project
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkipped()) {
            getLog().info("Ginger check skipped");
            return;
        }
        getLog().info("Running...");

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        try {
            List<URL> resources = ClasspathUtils.getResources(project, testClasspath);
            ClassLoader newCL = URLClassLoader.newInstance(resources.toArray(new URL[resources.size()]), Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(newCL);

            Class<?> returnType;
            try {
                returnType = Thread.currentThread().getContextClassLoader().loadClass(this.returnType);
            } catch (Exception e) {
                throw new MojoExecutionException("Can't load returnType: " + this.returnType, e);
            }
            Class<?> localizableType;
            try {
                localizableType = Thread.currentThread().getContextClassLoader().loadClass(this.localizableType);
            } catch (Exception e) {
                throw new MojoExecutionException("Can't load returnType: " + this.localizableType, e);
            }

            Class<? extends Annotation> keyType;
            try {
                keyType = Thread.currentThread().getContextClassLoader().loadClass(this.keyType).asSubclass(Annotation.class);
            } catch (Exception e) {
                throw new MojoExecutionException("Can't load localizableType: " + this.keyType, e);
            }

            Class<? extends Annotation> pluralCountType;
            try {
                pluralCountType = Thread.currentThread().getContextClassLoader().loadClass(this.pluralCountType).asSubclass(Annotation.class);
            } catch (Exception e) {
                throw new MojoExecutionException("Can't load pluralCountType: " + this.pluralCountType, e);
            }

            final DirectoryScanner directoryScanner = new DirectoryScanner();
            directoryScanner.setIncludes(includeFiles);
            directoryScanner.setBasedir(workingDirectory);
            directoryScanner.scan();
            for (String fileStr : directoryScanner.getIncludedFiles()) {
                String javaFileStr = fileStr.replaceAll("\\.properties", "\\.java");
                File propertyFile = new File(workingDirectory + "/" + fileStr);
                File outputDirectoryFile = new File(outputDirectory);
                outputDirectoryFile.mkdirs();
                File javaFile = new File(outputDirectoryFile, javaFileStr);
                if (javaFile.exists() && propertyFile.exists() && propertyFile.lastModified() < javaFile.lastModified()) {
                    getLog().info("No changes in " + fileStr);
                    continue;
                }
                try {
                    String javaClassName = javaFileStr.replaceAll("\\.java", "");
                    javaClassName = javaClassName.replaceAll("\\\\", ".");
                    javaClassName = javaClassName.replaceAll("/", ".");
                    getLog().info("Generating " + javaClassName + " ...");

                    InterfaceGenerator interfaceGenerator = new InterfaceGenerator();
                    interfaceGenerator.setReturnClass(returnType);
                    interfaceGenerator.setLocalizableClass(localizableType);
                    interfaceGenerator.setKeyClass(keyType);
                    interfaceGenerator.setPluralCountClass(pluralCountType);
                    interfaceGenerator.setup(javaClassName, propertyFile, outputDirectoryFile);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(byteArrayOutputStream);
                    interfaceGenerator.generate(ps);
                    getLog().debug(byteArrayOutputStream.toString());
                } catch (Exception e) {
                    getLog().error(e);
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Can't generate ginger interfaces", e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldCL);
        }

        // Add generated directory to source
        List<String> resources = project.getCompileSourceRoots();
        resources.add(outputDirectory);
        getLog().info("Done");
    }

    private boolean isSkipped() {
        return skip;
    }
}
