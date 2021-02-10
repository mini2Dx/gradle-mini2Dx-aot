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

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class AotMetadataTask extends DefaultTask {
    @InputDirectory
    @Optional
    final DirectoryProperty classesDir;
    @OutputFile
    @Optional
    final RegularFileProperty dependencyInjectionOutputFile;
    @OutputFile
    @Optional
    final RegularFileProperty serializationOutputOutputFile;

    @TaskAction
    def generateMetadata() {

    }
}
