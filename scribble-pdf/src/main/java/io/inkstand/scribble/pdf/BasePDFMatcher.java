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
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Base class for matchers based on PDFBox.
 */
class BasePDFMatcher extends BaseMatcher<PDF> {

    @Override
    public boolean matches(final Object o) {

        if (!(o instanceof PDF)) {
            return false;
        }
        final PDF pdf = (PDF) o;
        return matches(pdf);
    }

    protected boolean matches(PDF pdf) {
        try (InputStream is = (pdf.openStream())) {
            final PDDocument doc = PDDocument.load(is);
            return matchesPDF(doc);
        } catch (IOException e) {
            //ommit exception
            return false;
        }
    }

    /**
     * Override this method to provide additional pDF verification logic
     *
     * @param doc
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
