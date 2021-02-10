/*******************************************************************************
 * Copyright 2020 Thomas Cashman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.aot

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class AotPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply(plugin:'java')

        def extension = project.extensions.create("aot", AotExtension, project);

        project.getTasks().register("generateAotMetadata", AotMetadataTask.class, new Action<AotMetadataTask>() {
            public void execute(AotMetadataTask task) {
                task.dependencyInjectionOutputFile = extension.dependencyInjectionOutputFile;
                task.serializationOutputFile = extension.serializationOutputFile;
                task.classesDir = extension.classesDir;
                task.scanPackage = extension.scanPackage;
            }
        });
    }
}
