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

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 */
public class PDFMatchersTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private Path validPdf;
    private Path invalidPdf;

    @Before
    public void setUp() throws Exception {
        File file = folder.newFile("test.pdf");
        try(InputStream is = PDFMatchersTest.class.getResourceAsStream("PDFMatchersTest.pdf")){
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        this.validPdf = file.toPath();
        this.invalidPdf = folder.newFile("noPdf.txt").toPath();
    }

    @Test
    public void testIsPdf_valid_true() throws Exception {
        //prepare

        //act
        assertThat(validPdf, PDFMatchers.isPdf());

        //assert

    }


    @Test
    public void testIsPdf_invalid_false() throws Exception {
        //prepare

        //act
        assertThat(invalidPdf, not(PDFMatchers.isPdf()));

        //assert

    }


    @Test
    public void testIsPdf_noPath_false() throws Exception {
        //prepare

        //act
        assertThat(new Object(), not((Matcher<? super Object>) PDFMatchers.isPdf()));

        //assert

    }
}
