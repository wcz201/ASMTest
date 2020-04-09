package com.example.asmtest;


import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class ASMTest {
    @Test
    public void test() {
        try {
            FileInputStream fis = new FileInputStream("D:\\Example\\ASMTest\\app\\src\\test\\java\\com\\example\\asmtest\\MyClass.class");
            ClassReader classReader = new ClassReader(fis);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            classReader.accept(new MyClassVisitor(7 << 16 | 0 << 8, classWriter), ClassReader.EXPAND_FRAMES);
            byte[] bytes = classWriter.toByteArray();
            FileOutputStream fos = new FileOutputStream("D:\\Example\\ASMTest\\app\\src\\test\\java\\com\\example\\asmtest\\MyClass1.class");
            fos.write(bytes);
            fos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(int i) {
            super(i);
        }

        public MyClassVisitor(int i, ClassVisitor classVisitor) {
            super(i, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
            MethodVisitor methodVisitor = super.visitMethod(i, s, s1, s2, strings);
            System.out.println(s);
            return new MyMethod(api, methodVisitor, i, s, s1);
        }

        public class MyMethod extends AdviceAdapter {

            protected MyMethod(int i, MethodVisitor methodVisitor, int i1, String s, String s1) {
                super(i, methodVisitor, i1, s, s1);
            }

            int s;

            @Override
            protected void onMethodEnter() {
                super.onMethodEnter();

                invokeStatic(Type.getType("Ljava/lang/System;"), new Method("currentTimeMillis", "()J"));
                s = newLocal(Type.LONG_TYPE);
                storeLocal(s);

            }

            int s2;

            @Override
            protected void onMethodExit(int opcode) {
                super.onMethodExit(opcode);
                super.endMethod();
                invokeStatic(Type.getType("Ljava/lang/System;"), new Method("currentTimeMillis", "()J"));
                s2 = newLocal(Type.LONG_TYPE);
                storeLocal(s2);
                /*
                *  GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
                    LLOAD 3
                    LLOAD 1
                    LSUB
                    INVOKEVIRTUAL java/io/PrintStream.println (J)V
                * */
                getStatic(Type.getType("Ljava/lang/System;"), "out", Type.getType("Ljava/io/PrintStream;"));

                loadLocal(s);
                loadLocal(s2);
                math(SUB, Type.LONG_TYPE);
                invokeVirtual(Type.getType("Ljava/io/PrintStream;"), new Method("println", "(J)V"));
            }
        }

    }


}
