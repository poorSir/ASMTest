package com.example.asm

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.transforms.CustomClassTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class MethodDurationTimePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def appPlugin =project.plugins.hasPlugin(AppPlugin)
        if(appPlugin){
            def android = project.extensions.findByType(AppExtension)
            android?.registerTransform(new MethodDurationTimeTransform())
        }
    }
}
