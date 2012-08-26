package fi.vincit.jmobster.util;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Class that writes strings to a file or given OutputStream. Also
 * handles indentation. Default indentation is four spaces. The
 * indentation character and length can be configured with {@link DataWriter#setIndentationChar(char, int)}
 * method. Also line change characters can be changed with {@link DataWriter#setLineSeparator(String)}.
 */
public abstract class StreamDataWriter implements DataWriter {

    private static final Logger LOG = LoggerFactory.getLogger( StreamDataWriter.class );

    private BufferedWriter writer;
    private int indentationInChars;
    private int indentationInUnits;
    private boolean isLineIndented;
    private char indentationChar = ' ';
    private String lineSeparator = "\n";

    protected void initializeStream(OutputStream outputStream) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        writer = new BufferedWriter(outputStreamWriter);
    }
    protected void initializeBuffer( BufferedWriter bufferedWriter ) {
        this.writer = bufferedWriter;
    }

    protected StreamDataWriter() {
        indentationInChars = 4;
        indentationInUnits = 0;
    }

    protected BufferedWriter getBuffer() {
        return this.writer;
    }

    @Override
    @SuppressWarnings( "EmptyMethod" )
    public void open() {
        // Nothing to do
    }

    private void indentIfNeeded() {
        if( !isLineIndented ) {
            int spacesNeeded = indentationInChars * indentationInUnits;
            for( int i = 0; i < spacesNeeded; ++i ) {
                writeInternal( indentationChar );
            }
            isLineIndented = true;
        }
    }

    private void writeInternal(char c) {
        try {
            writer.write(c);
        } catch (IOException e) {
            LOG.error("Could not write", e);
        }
    }

    private void writeInternal(String string) {
        try {
            writer.write(string);
        } catch (IOException e) {
            LOG.error("Could not write", e);
        }
    }

    @Override
    public DataWriter write( String modelString ) {
        indentIfNeeded();
        writeInternal( modelString );
        return this;
    }

    @Override
    public DataWriter write( String modelString, String separator, boolean writeSeparator ) {
        write(modelString);
        if( writeSeparator ) {
            write(separator);
        }
        return this;
    }

    @Override
    public DataWriter writeLine( String modelStringLine ) {
        indentIfNeeded();
        writeInternal( modelStringLine );
        writeInternal( lineSeparator );
        isLineIndented = false;
        return this;
    }

    @Override
    public DataWriter writeLine( String modelStringLine, String separator, boolean writeSeparator ) {
        write(modelStringLine);
        if( writeSeparator ) {
            writeLine(separator);
        } else {
            writeLine("");
        }
        return this;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch( IOException e ) {
            LOG.error("Error", e);
        }

    }

    @Override
    public void setIndentation( int characterCount ) {
        this.indentationInChars = characterCount;
    }

    @Override
    public DataWriter indent() {
        ++indentationInUnits;
        return this;
    }
    @Override
    public DataWriter indentBack() {
        if( indentationInUnits > 0 ) {
            --indentationInUnits;
        }
        if( isLineIndented ) {
            writeLine("");
        }
        return this;
    }

    @Override
    public void setIndentationChar( char indentationChar, int characterCount ) {
        this.indentationInChars = characterCount;
        this.indentationChar = indentationChar;
    }

    @Override
    public void setLineSeparator( String lineSeparator ) {
        this.lineSeparator = lineSeparator;
    }

    @Override
    public String toString() {
        try {
            this.writer.flush();
            return this.writer.toString();
        } catch( IOException e ) {
        }
        return "";
    }
}
