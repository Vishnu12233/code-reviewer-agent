import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;


public class CodeReviewerAgent1 {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java CodeReviewerAgent <file.java>");
            return;
        }
        Path p = Path.of(args[0]);
        if (!Files.exists(p)) {
            System.out.println("File not found: " + p);
            return;
        }

        String text = Files.readString(p);
        LineIndex index = new LineIndex(text);

        List<Rule> rules = List.of(
            new MissingDotRule(),
            new SystemOutRule(),
            new UnclosedConstructRule()
        );

        List<Suggestion> suggestions = new ArrayList<>();
        for (Rule rule : rules) {
            suggestions.addAll(rule.apply(text, index));
        }

        if (suggestions.isEmpty()) {
            System.out.println("No quick suggestions for " + p.getFileName());
            return;
        }

        suggestions.sort(Comparator.comparingInt(s -> s.line));
        System.out.println("Suggestions for " + p.getFileName() + ":");
        for (Suggestion s : suggestions) {
            System.out.printf("  Line %d: %s%n", s.line, s.msg);
        }
    }

    // ---------- Core Types ----------
    interface Rule {
        List<Suggestion> apply(String text, LineIndex index);
    }

    static class Suggestion {
        final int line;
        final String msg;
        Suggestion(int line, String msg) { this.line = line; this.msg = msg; }
    }

    static class LineIndex {
        private final int[] lineStarts;
        LineIndex(String text) {
            List<Integer> starts = new ArrayList<>();
            starts.add(0);
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '\n') starts.add(i + 1);
            }
            lineStarts = starts.stream().mapToInt(Integer::intValue).toArray();
        }
        int lineOf(int pos) {
            int idx = Arrays.binarySearch(lineStarts, pos);
            if (idx < 0) idx = -idx - 2;
            return idx + 1;
        }
    }

    // ---------- Rules ----------
    static class MissingDotRule implements Rule {
        private static final Pattern P = Pattern.compile(
            "\\b([A-Za-z_][A-Za-z0-9_]*)\\s+([A-Za-z_][A-Za-z0-9_]*)\\b"
        );
        private static final Set<String> KEYWORDS = Set.of(
            "new","return","if","for","while","switch","case","throws",
            "throw","import","package","class","interface"
        );

        @Override
        public List<Suggestion> apply(String text, LineIndex index) {
            List<Suggestion> out = new ArrayList<>();
            Matcher m = P.matcher(text);
            while (m.find()) {
                String lhs = m.group(1), rhs = m.group(2);
                if (KEYWORDS.contains(lhs)) continue;
                // Heuristic: only warn if followed by '(' (method call)
                int end = m.end();
                if (end < text.length() && text.charAt(end) == '(') {
                    out.add(new Suggestion(index.lineOf(m.start()),
                        "Possible missing '.' between '" + lhs + "' and '" + rhs + "'"));
                }
            }
            return out;
        }
    }

    static class SystemOutRule implements Rule {
        private static final Pattern P = Pattern.compile(
            "\\bSystem\\s+out\\s+println\\b"
        );
        @Override
        public List<Suggestion> apply(String text, LineIndex index) {
            List<Suggestion> out = new ArrayList<>();
            Matcher m = P.matcher(text);
            while (m.find()) {
                out.add(new Suggestion(index.lineOf(m.start()),
                    "Replace 'System out println' with 'System.out.println'"));
            }
            return out;
        }
    }

    static class UnclosedConstructRule implements Rule {
        private static final Map<Character,Character> PAIRS = Map.of(
            '(', ')', '{', '}', '[', ']'
        );
        @Override
        public List<Suggestion> apply(String text, LineIndex index) {
            List<Suggestion> out = new ArrayList<>();
            Deque<Frame> stack = new ArrayDeque<>();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '/' && i+1 < text.length()) {
                    if (text.charAt(i+1) == '/') { // line comment
                        while (i < text.length() && text.charAt(i) != '\n') i++;
                        continue;
                    } else if (text.charAt(i+1) == '*') { // block comment
                        int j = text.indexOf("*/", i+2);
                        i = (j == -1 ? text.length() : j+1);
                        continue;
                    }
                }
                if (PAIRS.containsKey(c)) {
                    stack.push(new Frame(c, i));
                } else if (PAIRS.containsValue(c)) {
                    if (!stack.isEmpty() && PAIRS.get(stack.peek().ch) == c) {
                        stack.pop();
                    } else {
                        out.add(new Suggestion(index.lineOf(i), "Unmatched '" + c + "'"));
                    }
                } else if (c == '"' || c == '\'') {
                    int j = skipString(text, i, c);
                    if (j == -1) {
                        out.add(new Suggestion(index.lineOf(i),
                            "Unclosed " + (c=='"'?"string":"char") + " literal"));
                        break;
                    } else i = j;
                }
            }
            while (!stack.isEmpty()) {
                Frame f = stack.pop();
                out.add(new Suggestion(index.lineOf(f.pos), "Unclosed '" + f.ch + "'"));
            }
            return out;
        }
        private int skipString(String s, int start, char quote) {
            int i = start+1;
            while (i < s.length()) {
                char c = s.charAt(i);
                if (c == '\\') { i+=2; continue; }
                if (c == quote) return i;
                i++;
            }
            return -1;
        }
        static class Frame { final char ch; final int pos; Frame(char c,int p){ch=c;pos=p;} }
    }
}