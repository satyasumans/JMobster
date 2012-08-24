package fi.vincit.jmobster.processor.frameworks.html5.validator.writer;

/*
 * Copyright 2012 Juha Siponen
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

import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.html.HTML5Writer;

import javax.validation.constraints.Size;

public class HTML5ValidatorWriterManager extends BaseValidatorWriterManager<HTML5Writer> {
    public HTML5ValidatorWriterManager( HTML5Writer modelWriter ) {
        super( modelWriter );
        setValidator(
                new SizeValidatorWriter()
        );
    }
}
