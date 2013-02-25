/*
 * Copyright 2013, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib2.dexbacked.raw;

import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;

import javax.annotation.Nonnull;

public class TypeIdItem {
    public static final int ITEM_SIZE = 4;

    public static SectionAnnotator getAnnotator() {
        return new SectionAnnotator() {
            @Override
            public void annotateSection(@Nonnull AnnotatedBytes out, @Nonnull DexBackedDexFile dexFile, int length) {
                if (length > 0) {
                    out.annotate(0, "-----------------------------");
                    out.annotate(0, "type_id_item section");
                    out.annotate(0, "-----------------------------");
                    out.annotate(0, "");

                    for (int i=0; i<length; i++) {
                        out.annotate(0, "[%d] type_id_item", i);
                        out.indent();
                        annotateString(out, dexFile);
                        out.deindent();
                    }
                }
            }
        };
    }

    private static void annotateString(@Nonnull AnnotatedBytes out, @Nonnull DexBackedDexFile dexFile) {
        int stringIndex = dexFile.readSmallUint(out.getCursor());
        out.annotate(4, StringIdItem.getReferenceAnnotation(dexFile, stringIndex));
    }

    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
        try {
            String typeString = dexFile.getType(typeIndex);
            return String.format("type_id_item[%d]: \"%s\"", typeIndex, StringUtils.escapeString(typeString));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return String.format("type_id_item[%d]", typeIndex);
    }
}
