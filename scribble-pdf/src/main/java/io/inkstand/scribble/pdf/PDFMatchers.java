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

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.hamcrest.Description;
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

    /**
     * Creates a matcher that verifies, if a PDF has a specific number of pages
     * @param pageCount
     *  the number of expected pages
     * @return
     *  a matcher verifying the pages of a PDF
     */
    public static Matcher<? super PDF> hasPages(final int pageCount) {

        return new BasePDFMatcher(){

            @Override
            protected boolean matchesPDF(final PDDocument doc) {

                return doc.getNumberOfPages() == pageCount;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Expected " + pageCount + " pages");
            }

        };
    }

    public static Matcher<? super PDF> conformsTo(final PDFAConformance conformanceLevel) {
        return new BasePDFMatcher() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is a valid " + conformanceLevel.getFormat().toString() + " document");
            }

            @Override
            protected boolean matches(PDF pdf) {
                try {
                    final PreflightParser parser = new PreflightParser(pdf.toDataSource());
                    parser.parse(conformanceLevel.getFormat());
                    final PreflightDocument doc = parser.getPreflightDocument();
                    final ValidationResult result = doc.getResult();
                    return result.isValid();
                } catch (IOException e) {
                    return false;
                }
            }

        };

    }
}
