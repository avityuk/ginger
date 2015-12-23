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

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Test;

import java.io.File;

public class MavenGeneratorTest extends AbstractMojoTestCase {

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething()
            throws Exception {
        File projectBase = getTestFile("src/test/resources/com/vityuk/ginger/maven/");
        File pom = new File(projectBase, "pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
        ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
        ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
        MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();

        MavenGeneratorMojo myMojo = (MavenGeneratorMojo) lookupConfiguredMojo(project, "ginger-generator");
        assertNotNull(myMojo);

        myMojo.execute();

        File outputDirectory = new File(myMojo.outputDirectory);
        File resultFile = new File(outputDirectory, "com/vityuk/ginger/test1/GeneralConstants.java");
        assertTrue("GeneralConstants.java is NOT generated", resultFile.exists());
    }
}