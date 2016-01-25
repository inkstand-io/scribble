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
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Library containing hamcrest matchers for operating with PDF files.
 */
public class PDFMatchers {

    /**
     * Creates a matcher that verifies, if a file identified by a {@link java.nio.file.Path}
     * is a valid PDF document.
     * @return
     *  a matcher verifying a file to be a PDF
     */
    public static Matcher<? super Path> isPdf() {

        return new BaseMatcher<Path>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof Path)) {
                    return false;
                }
                Path path = (Path) o;
                try (InputStream is = Files.newInputStream(path)) {
                    PDDocument.load(is);
                    return true;
                } catch (IOException e) {
                    //ommit exception
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is a PDF document");
            }
        };
    }
}
