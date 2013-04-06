package fi.vincit.jmobster.processor.languages;

import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * The basic usage for BaseDataWriter is when you are creating a higher level
 * DataWriter for some language. Then you would create the DataWriter:
 * <pre>public class JavaScriptWriter extends BaseDataWriter&lt;JavaScriptWriter&gt;</pre>.
 * The purpose of the generic type parameter is to allow automatically delegate
 * the existing DataWriter methods to a concrete writer and at the same time allowing
 * the new writer to implement chaining with the correct type. Without the generics type
 * parameter the methods would return a plain DataWriter which would have to manually
 * casted to the wanted writer.
 *
 * @param <T> DataWriter to use. Usually the one you are extending.
 */
public abstract class BaseDataWriter<T extends DataWriter> implements DataWriter {
    private final DataWriter writer;

    protected BaseDataWriter(DataWriter writer) {
        this.writer = writer;
    }

    @Override
    public boolean isOpen() {
        return writer.isOpen();
    }

    @Override
    public T write(char c) {
        writer.write(c);
        return (T)this;
    }

    @Override
    public T write(String modelString) {
        writer.write(modelString);
        return (T)this;
    }

    @Override
    public T write(String modelString, String separator, boolean writeSeparator) {
        writer.write(modelString, separator, writeSeparator);
        return (T)this;
    }

    @Override
    public T writeLine(String modelStringLine) {
        writer.writeLine(modelStringLine);
        return (T)this;
    }

    @Override
    public T writeLine(String modelStringLine, String separator, boolean writeSeparator) {
        writer.writeLine(modelStringLine, separator, writeSeparator);
        return (T)this;
    }

    @Override
    public void close() {
        writer.close();
    }

    @Override
    public void setIndentation(int spaces) {
        writer.setIndentation(spaces);
    }

    @Override
    public T indent() {
        writer.indent();
        return (T)this;
    }

    @Override
    public T indentBack() {
        writer.indentBack();
        return (T)this;
    }

    @Override
    public void setIndentationChar(char indentationChar, int characterCount) {
        writer.setIndentationChar(indentationChar, characterCount);
    }

    @Override
    public void setLineSeparator(String lineSeparator) {
        writer.setLineSeparator(lineSeparator);
    }

    @Override
    public String toString() {
        return writer.toString();
    }

}