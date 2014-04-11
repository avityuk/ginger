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
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

@Mojo(name = "ginger-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
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
     * The associated maven project
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Running...");

        final DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setIncludes(includeFiles);
        directoryScanner.setBasedir(workingDirectory);
        directoryScanner.scan();
        for (String fileStr : directoryScanner.getIncludedFiles()) {
            String javaFileStr = fileStr.replaceAll("\\.properties", "");
            File propertyFile = new File(workingDirectory + "/" + fileStr);
            File outputFile = new File(outputDirectory);
            outputFile.mkdirs();
            File javaFile = new File(outputFile, javaFileStr);
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
                interfaceGenerator.setup(javaClassName, propertyFile, outputFile);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(byteArrayOutputStream);
                interfaceGenerator.generate(ps);
                getLog().debug(byteArrayOutputStream.toString());
            } catch (Exception e) {
                getLog().error(e);
            }
        }

        // Add generated directory to source
        List<String> resources = project.getCompileSourceRoots();
        resources.add(outputDirectory);
        getLog().info("Done");
    }
}
