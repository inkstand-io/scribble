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

import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Test;

/**
 *
 */
public class PDFMatchersTest {

    private URL resource(final String resource) {

        return PDFMatchersTest.class.getResource(resource);
    }

    @Test
    public void testIsPdf() throws Exception {

        //prepare
        PDF pdf = PDF.of(resource("BasePDFMatchers_isPDFTest.pdf"));

        //act & assert
        assertThat(pdf, PDFMatchers.isPdf());
    }

    @Test
    public void testHasPages_3() throws Exception {
        //prepare

        PDF pdf = PDF.of(resource("BasePDFMatchers_hasPagesTest.pdf"));
        //act & assert
        assertThat(pdf, PDFMatchers.hasPages(3));
    }


}
