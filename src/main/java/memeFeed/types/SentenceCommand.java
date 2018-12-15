package memeFeed.types;

import memeFeed.utils.PorterStemmer;

import java.util.ArrayList;
import java.util.List;

public class SentenceCommand {

    public List<String[]> _words = new ArrayList<>();

    public SentenceCommand() {
        this("");
    }

    public SentenceCommand(String... words) {
        if (words == null) return;
        _words.add(words);
    }

    public SentenceCommand addWords(String... words) {
        _words.add(words);
        return this;
    }

    public Boolean isCommandFound(String string) {
        string = string.toLowerCase();
        // First change everything to the stem of the word
        char[] messageChars = string.toCharArray();
        string = "";
        PorterStemmer stem = new PorterStemmer();
        for (char ch : messageChars) {
            if (Character.isLetter((char) ch)) {
                stem.add(Character.toLowerCase((char) ch));
            } else {
                stem.stem();
                string += stem.toString() + " ";
                stem.reset();
            }
        }
        // Then stem last word
        stem.stem();
        string += stem.toString() + " ";
        // Then check for command
        Boolean overallFound = false;
        for (String[] wordSet : _words) {
            Boolean found = false;
            for (String word : wordSet) {
                word = word.toLowerCase();
                if (found) break;
                if ((string.contains(word) && (
                        string.contains(word + " ") ||
                        string.contains(" " + word) ||
                        string.contains(word + " ") ||
                        string.contains(word + ".") ||
                        string.contains(word + "!") ||
                        string.contains(word + "?")
                ))) {
                    found = true;
                }
            }
            overallFound = found;
            if (!found) return false;
        }
        return overallFound;
        /*for (String word : _words) {
            if (!(string.contains(word) && (
                    string.contains(word+ " ") ||
                    string.contains(" " + word) ||
                    string.contains(word + " ") ||
                    string.contains(word + ".") ||
                    string.contains(word + "!") ||
                    string.contains(word + "?")
            ))) {
                return false;
            }
        }*/
    }
}
