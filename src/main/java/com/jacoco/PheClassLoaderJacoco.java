/**
 * Special Declaration: These technical material reserved as the technical
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law"
 * "ordinances on Protection of Computer Software" and other relevant
 * administrative regulations and international treaties. Without the written
 * permission of the Company, no person may use (including but not limited to
 * the illegal copy, distribute, display, image, upload, and download) and
 * disclose the above technical documents to any third party. Otherwise, any
 * infringer shall afford the legal liability to the company.
 * <p>
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 * <p>
 * Copyright(C) 2012 Bankit Tech, All rights reserved.
 */
package com.jacoco;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * <DL>
 * <DT><B>phoenix class loader</B></DT>
 * <p>
 * <DD>详细介绍</DD>
 * </DL>
 * <p>
 *
 * <DL>
 * <DT><B>范例</B></DT>
 * <p>
 * <DD>范例说明</DD>
 * </DL>
 * <p>
 *
 * @author 江成
 * @author 浙江宇信班克信息技术有限公司
 * @version 1.0.0
 */
public class PheClassLoaderJacoco extends ClassLoader {

    public static PheClassLoaderJacoco pheClassLoaderJacoco = new PheClassLoaderJacoco();

    /**
     * A class loader that loads classes from in-memory data.
     */

    private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

    /**
     * Add a in-memory representation of a class.
     *
     * @param name  name of the class
     * @param bytes class definition
     */
    public void addDefinition(final String name, final byte[] bytes) {
        definitions.put(name, bytes);
    }

    @Override
    protected Class<?> loadClass(final String targetName, final boolean resolve)
            throws ClassNotFoundException {
        if(targetName.startsWith("java")){
            return super.loadClass(targetName, resolve);
        }
        try {
            InputStream original = getTargetClass(targetName);
            byte[] instrumented = PheJacocoThread1.instr.instrument(original, targetName);
            original.close();
            pheClassLoaderJacoco.addDefinition(targetName, instrumented);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[] bytes = definitions.get(targetName);
        if (bytes != null) {
            return defineClass(targetName, bytes, 0, bytes.length);
        }
        return super.loadClass(targetName, resolve);
    }


    private InputStream getTargetClass(final String name) {
        final String resource = '/' + name.replace('.', '/') + ".class";
        return getClass().getResourceAsStream(resource);
    }


}
