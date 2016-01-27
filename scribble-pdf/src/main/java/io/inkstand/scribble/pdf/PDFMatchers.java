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

            public int actualNumPages;

            @Override
            protected boolean matchesPDF(final PDDocument doc) {
                this.actualNumPages = doc.getNumberOfPages();
                return doc.getNumberOfPages() == pageCount;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(pageCount + " pages");
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText(this.actualNumPages + " pages");
            }
        };
    }

    /**
     * Creates a matcher for verifying if a document is valid against a PDF/A conformance level.
     *
     * @param conformanceLevel
     *         the expected conformance level
     *
     * @return a matcher verifying the PDF/A conformance
     */
    public static Matcher<? super PDF> conformsTo(final PDFALevel conformanceLevel) {
        return new BasePDFMatcher() {
            public ValidationResult validationResult;

            @Override
            public void describeTo(Description description) {
                description.appendText("a valid " + conformanceLevel.getFormat().toString() + " document");
            }

            @Override
            protected boolean matches(PDF pdf) {
                try {
                    final PreflightParser parser = new PreflightParser(pdf.toDataSource());
                    parser.parse(conformanceLevel.getFormat());
                    final PreflightDocument doc = parser.getPreflightDocument();
                    doc.validate();
                    final ValidationResult result = doc.getResult();
                    this.validationResult = result;
                    return result.isValid();
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("document does not conform to " + conformanceLevel.toString() + ", because:\n");
                for (ValidationResult.ValidationError error : this.validationResult.getErrorsList()) {
                    description.appendText("-").appendText(error.getDetails()).appendText("\n");
                }
            }
        };

    }
}
