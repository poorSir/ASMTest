package com.example.asm

import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.MethodVisitor
import groovyjarjarasm.asm.Opcodes

class MethodDurationTimeClassVisitor extends ClassVisitor{
    MethodDurationTimeClassVisitor(int api) {
        super(api)
    }

    MethodDurationTimeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access,name,descriptor,signature,exceptions)
        if(name == "test"){
            return new MethodDurationTimeMethodVisitor(Opcodes.ASM7,methodVisitor,access,name,descriptor)
        }
        return methodVisitor

    }
}
