import java.util.stream.Stream;

public class StringUtils {

    private String _internal;


    public StringUtils() {
        this._internal="";
    }

    public StringUtils(String initial) {
        this._internal=initial;
    }

    /**
     * appends a character to the string
     * @param chr is the character to append;
     * @return this
     */
    public  StringUtils append(char chr) {
        this._internal = this._internal + Character.toString(chr);
        return this;
    }


    /**
     * Get the char at index
     * @param index index of the char
     * @return the character at index
     */
    public char charAt(int index) {
        if (index < 0 || index > _internal.length()) {
            throw new IndexOutOfBoundsException("");
        } else {
            return this._internal.charAt(index);
        }
    }

    /**
     * Delete the char at the specific position.
     * @param index of the char
     * @return modified string
     */
    public StringUtils deleteCharAt(int index) {
        if (index < 0 || index > _internal.length()) {
            throw new IndexOutOfBoundsException("");
        } else {
            this._internal = this._internal.substring(0,index) +
                    this._internal.substring(index+1, _internal.length());
            return this;
        }
    }


    /**
     * Reverse the string
     * @return this
     */
    public StringUtils reverse() {
        this._internal = new StringBuilder(this._internal)
                .reverse()
                .toString();
        return this;
    }

    /**
     * Return
     * @return
     */
    @Override
    public String toString() {
        return _internal;
    }
}
