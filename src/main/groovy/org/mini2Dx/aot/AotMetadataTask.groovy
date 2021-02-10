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

import javafx.beans.property.ListProperty
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.mini2Dx.core.di.BasicComponentScanner
import org.mini2Dx.core.di.annotation.Prototype
import org.mini2Dx.core.di.annotation.Singleton
import org.mini2Dx.core.serialization.AotSerializationData
import org.reflections.Reflections

import java.lang.reflect.Field

class AotMetadataTask extends DefaultTask {
    @Input
    final String scanPackage;
    @Input
    @Optional
    final RegularFileProperty mergeDependencyInjectionFile;
    @Input
    @Optional
    final RegularFileProperty mergeSerializationFile;
    @OutputFile
    @Optional
    final RegularFileProperty dependencyInjectionOutputFile;
    @OutputFile
    @Optional
    final RegularFileProperty serializationOutputFile;

    @TaskAction
    def generateMetadata() {
        Set<File> classpath = project.getConfigurations().getByName("compileClasspath").getFiles();
        List<URL> classpathUrls = new ArrayList<>();

        for(File classFile : classpath) {
            classpathUrls.add(classFile.toURI().toURL());
        }

        BasicComponentScanner dependencyInjectionClasses = new BasicComponentScanner();


        AotSerializationData.registerClass(UiTheme.class);
        AotSerializationData.registerClass(AnimatedImage.class);
        AotSerializationData.registerClass(Button.class);
        AotSerializationData.registerClass(Checkbox.class);
        AotSerializationData.registerClass(Container.class);
        AotSerializationData.registerClass(CustomUiElement.class);
        AotSerializationData.registerClass(Div.class);
        AotSerializationData.registerClass(FlexRow.class);
        AotSerializationData.registerClass(Image.class);
        AotSerializationData.registerClass(ImageButton.class);
        AotSerializationData.registerClass(Label.class);
        AotSerializationData.registerClass(ParentUiElement.class);
        AotSerializationData.registerClass(ProgressBar.class);
        AotSerializationData.registerClass(RadioButton.class);
        AotSerializationData.registerClass(ScrollBox.class);
        AotSerializationData.registerClass(Select.class);
        AotSerializationData.registerClass(Slider.class);
        AotSerializationData.registerClass(Tab.class);
        AotSerializationData.registerClass(TabButton.class);
        AotSerializationData.registerClass(TabView.class);
        AotSerializationData.registerClass(TextBox.class);
        AotSerializationData.registerClass(TextButton.class);
        AotSerializationData.registerClass(UiElement.class);

        Reflections reflections = new Reflections(scanPackage, classpathUrls.toArray());

        for(Class clazz : reflections.getTypesAnnotatedWith(Singleton.class)) {
            dependencyInjectionClasses.getSingletonClasses().add(clazz);
        }
        for(Class clazz : reflections.getTypesAnnotatedWith(Prototype.class)) {
            dependencyInjectionClasses.getPrototypeClasses().add(clazz);
        }

        for(Field field : reflections.getFieldsAnnotatedWith(org.mini2Dx.core.reflect.Field.class)) {
            AotSerializationData.registerClass(field.getDeclaringClass());
        }

        PrintWriter dependencyInjectionWriter = new PrintWriter(dependencyInjectionOutputFile.getOrElse(project.file("build/aot.di")));
        dependencyInjectionClasses.saveTo(dependencyInjectionWriter);
        dependencyInjectionWriter.flush();
        dependencyInjectionWriter.close();

        PrintWriter serializationWriter = new PrintWriter(serializationOutputFile.getOrElse(project.file("build/aot.ser")));
        AotSerializationData.saveTo(serializationWriter);

        RegularFile mergeSerializationFile = mergeSerializationFile.getOrNull();
        if(mergeSerializationFile != null) {
            Scanner scanner = new Scanner(mergeSerializationFile.asFile);
            while(scanner.hasNextLine()) {
                serializationWriter.println(scanner.nextLine());
            }
            scanner.close();
        }
        serializationWriter.flush();
        serializationWriter.close();
    }
}
