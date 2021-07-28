package com.example.asm

import groovyjarjarasm.asm.Label
import groovyjarjarasm.asm.MethodVisitor
import groovyjarjarasm.asm.Type
import groovyjarjarasm.asm.commons.AdviceAdapter

class MethodDurationTimeMethodVisitor extends AdviceAdapter{
    int  startTimeIndex=0
    int endTimeIndex = 0

    protected MethodDurationTimeMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor)
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        startTimeIndex = newLocal(Type.DOUBLE_TYPE)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        mv.visitVarInsn(LSTORE, startTimeIndex)

    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        endTimeIndex = newLocal(Type.DOUBLE_TYPE)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        mv.visitVarInsn(LLOAD, startTimeIndex)
        mv.visitInsn(LSUB)
        mv.visitVarInsn(LSTORE, endTimeIndex)
        mv.visitInsn(ICONST_0)
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        mv.visitVarInsn(LLOAD, endTimeIndex)
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false)
    }
}
