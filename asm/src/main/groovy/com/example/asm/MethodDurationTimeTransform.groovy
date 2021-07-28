package com.example.asm

import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import groovy.io.FileType
import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.ClassWriter
import groovyjarjarasm.asm.Opcodes
import org.apache.tools.ant.taskdefs.Available
import org.codehaus.groovy.classgen.AsmClassGenerator

class MethodDurationTimeTransform extends Transform{
    @Override
    String getName() {
        return "MethodDurationTimeTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {

        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        if(!isIncremental()) {
            outputProvider.deleteAll()
        }
        transformInvocation.inputs.forEach{input->
            input.jarInputs.each {jarInput->
                File dest = outputProvider.getContentLocation(jarInput.file.absolutePath,jarInput.contentTypes,jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
            input.directoryInputs.each {directoryInput->
                File dir = directoryInput.file
                if(dir){
                    dir.traverse(type: FileType.FILES,nameFilter:~/.*\.class/ ){
                        if(it.absolutePath.contains("com\\example\\myapplication\\MainActivity.class")){
                            ClassReader classReader = new ClassReader(it.bytes)
                            ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                            ClassVisitor classVisitor =  new MethodDurationTimeClassVisitor(Opcodes.ASM7,classWriter)
                            classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES)
                            byte[] bytes = classWriter.toByteArray()
                            FileOutputStream fileOutputStream = new FileOutputStream(it.path)
                            fileOutputStream.write(bytes)
                            fileOutputStream.close()
                        }
                    }
                }
                File dest = outputProvider.getContentLocation(directoryInput.file.absolutePath,directoryInput.contentTypes,directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }
    }
}
