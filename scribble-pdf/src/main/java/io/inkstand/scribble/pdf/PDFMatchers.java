/*
 * Copyright 2016 Gerald Muecke, gerald.muecke@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.pdf;

import org.hamcrest.Matcher;

/**
 * Library containing hamcrest matchers for operating with PDF files.
 */
public final class PDFMatchers {

    private PDFMatchers(){}

    /**
     * Creates a matcher that verifies, if a file identified by a {@link java.nio.file.Path}
     * is a valid PDF document.
     * @return
     *  a matcher verifying a file to be a PDF
     */
    public static Matcher<? super PDF> isPdf() {
        return new BasePDFMatcher();
    }



}
