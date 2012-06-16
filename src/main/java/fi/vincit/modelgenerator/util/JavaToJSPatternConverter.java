package fi.vincit.modelgenerator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;

/**
 * Converts Java regular expression pattern to
 * JavaScrip pattern
 */
public final class JavaToJSPatternConverter {
    private static final Logger LOG = LoggerFactory
            .getLogger( JavaToJSPatternConverter.class );

    private static final String JAVASCRIPT_REGEXP_START = "/";
    private static final String JAVASCRIPT_REGEXP_END = "/";
    // JavaScrip regular expression modifiers
    private static final String JAVASCRIPT_CASE_INSENSITIVE_MOD = "i";
    private static final String JAVASCRIPT_MULTI_LINE_MOD = "m";

    /**
     * Coverts the given regular expression patter from Java form
     * to JavaScript form. The only supported flags are at the moment
     * Pattern.Flag.CASE_INSENSITIVE and Pattern.Flag.MULTILINE. The current
     * version may not produce accurate results for all special metacharacters
     * but support will be added later.
     * @param javaPattern Java regular expression pattern
     * @param flags 0..N flags
     * @return Pattern in JavaScript form. If empty pattern is given, an empty pattern is returned.
     */
    public static String convertFromJava(String javaPattern, Pattern.Flag... flags ) {
        if( javaPattern.trim().length() == 0 ) {
            return convertToJSForm("");
        }
        return convertToJSForm(javaPattern) + getModifiersFromFlags(flags);
    }

    /**
     * Modifies the given Java pattern to JavaScript pattern
     * without modifiers.
     * @param javaPattern Java regular expression pattern
     * @return Pattern in JavaScript form;
     */
    private static String convertToJSForm(String javaPattern) {
        return JAVASCRIPT_REGEXP_START + javaPattern + JAVASCRIPT_REGEXP_END;
    }

    /**
     * Returns modifiers in JavaScript from
     * @param flags Java pattern flags
     * @return Modifier string. Empty string if no modifiers
     */
    private static String getModifiersFromFlags( Pattern.Flag... flags ) {
        String modifiers = "";
        for( Pattern.Flag flag : flags ) {
            if( Pattern.Flag.CASE_INSENSITIVE.equals(flag) ) {
                modifiers += JAVASCRIPT_CASE_INSENSITIVE_MOD;
            } else if( Pattern.Flag.MULTILINE.equals(flag) ) {
                modifiers += JAVASCRIPT_MULTI_LINE_MOD;
            } else {
                LOG.warn("Regular Expression flag " + flag.name() + " is not supported");
            }
        }
        return modifiers;
    }
}
