/*
 * SonarQube Checkstyle Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.checkstyle;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;

import com.google.common.collect.ImmutableList;

public class CheckstyleSensorTest {

  private RulesProfile profile = mock(RulesProfile.class);
  private DefaultFileSystem fileSystem = new DefaultFileSystem(new File(""));
  private CheckstyleSensor sensor = new CheckstyleSensor(profile, null, fileSystem);

  private Project project = new Project("projectKey");

  @Test
  public void shouldExecuteOnProjectWithoutJavaFileAndWithRule() {
    addOneActiveRule();
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();
  }

  @Test
  public void shouldExecuteOnProjectWithJavaFileAndWithoutRule() {
    addOneJavaFile();
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();
  }

  @Test
  public void shouldExecuteOnProjectWithJavaFilesAndRules() {
    addOneJavaFile();
    addOneActiveRule();
    assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
  }

  @Test
  public void testToString() {
    assertThat(new CheckstyleSensor(null, null, null).toString())
            .isEqualTo("CheckstyleSensor");
  }

  private void addOneJavaFile() {
    File file = new File("MyClass.java");
    fileSystem.add(new DefaultInputFile("", file.getName())
            .setLanguage("java").setType(Type.MAIN));
  }

  private void addOneActiveRule() {
    when(profile.getActiveRulesByRepository("checkstyle"))
            .thenReturn(ImmutableList.of(mock(ActiveRule.class)));
  }

}
