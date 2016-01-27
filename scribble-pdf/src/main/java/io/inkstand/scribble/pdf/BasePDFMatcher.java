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

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.slf4j.Logger;

/**
 * Base class for matchers based on PDFBox.
 */
class BasePDFMatcher extends BaseMatcher<PDF> {

    private static final Logger LOG = getLogger(BasePDFMatcher.class);

    @Override
    public boolean matches(final Object o) {

        if (!(o instanceof PDF)) {
            return false;
        }
        final PDF pdf = (PDF) o;
        return matches(pdf);
    }

    /**
     * Is invoked by the matches method when the type of the target object is verified. Override this method to add
     * verifications on the raw data instead of a loaded document.
     *
     * @param pdf
     *         the handle for the PDF data
     *
     * @return <code>true</code> if the pdf document is a valid PDF document.
     */
    protected boolean matches(PDF pdf) {
        try (InputStream is = (pdf.openStream())) {
            final PDDocument doc = PDDocument.load(is);
            return matchesPDF(doc);
        } catch (IOException e) {
            LOG.debug("Could not load PDF document", e);
            return false;
        }
    }

    /**
     * Override this method to provide additional PDF verification logic on the loaded PDF document.
     *
     * @param doc
     *  a PDF document (Apache PDFbox API)
     *
     * @return <code>true</code> if the PDF document matches the criteria
     */
    protected boolean matchesPDF(final PDDocument doc) {

        return true;
    }

    @Override
    public void describeTo(Description description) {

        description.appendText("a valid PDF document");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("not a PDF document");
    }
}
