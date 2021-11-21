import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CompleteScanner {

    enum TokenCodes {
        Keyword("K"), Operator("O"), Separator("P"), NumberLiteral("N"), StringLiteral("S"), Identifier("I");

        private String code;

        public String getCode() {
            return this.code;
        }

        TokenCodes(String code) {
            this.code = code;
        }
    }

    enum Tokens {
        FALSE("false", 0, "keyword"), TRUE("true", 1, "keyword"), CLASS("class", 2, "keyword"),
        PUBLIC("public", 3, "keyword"), STATIC("static", 4, "keyword"), THIS("this", 5, "keyword"),
        IF("if", 6, "keyword"), FOR("for", 7, "keyword"), NEW("new", 8, "keyword"),
        LGK("{", 50, "separator"), RGK("}", 51, "separator"), LEK("[", 52, "separator"),
        REK("]", 53, "separator"), LRK("(", 54, "separator"), RRK(")", 55, "separator"),
        DOT(".", 60, "separator"), COMMA(",", 61, "separator"), COLON(":", 62, "separator"),
        SEMICOLON(";", 63, "separator"), NOT("!", 70, "operator"), AND("&&", 71, "operator"),
        LESS("<", 72, "operator"), PLUS("+", 80, "operator"), MINUS("-", 81, "operator"),
        MULTIPLY("*", 82, "operator"), DIVIDE("/", 83, "operator"), VOID("void", 90, "keyword"),
        BOOLEAN("boolean", 91, "keyword"), INT("int", 92, "keyword"), ELSE("else", 9, "keyword"),
        EQUALS("=", 73, "operator");


        private String token;
        private int tokenId;
        private String type;

        Tokens(String token, int tokenId, String type) {
            this.token = token;
            this.tokenId = tokenId;
            this.type = type;
        }

        public String getToken() {
            return token;
        }

        public int getTokenId() {
            return tokenId;
        }

        public String getType() {
            return type;
        }
    }

    static class Token {
        String token;
        int row;
        String tokenCode;
        int tokenId;

        public Token(String token, int row, String tokenCode, int tokenId) {
            this.token = token;
            this.row = row;
            this.tokenCode = tokenCode;
            this.tokenId = tokenId;
        }

        public String getToken() {
            return token;
        }

        public int getRow() {
            return row;
        }

        public String getTokenCode() {
            return tokenCode;
        }

        public int getTokenId() {
            return tokenId;
        }
    }


    static List<Character> alphabet = new ArrayList<>();
    static List<Character> numericAlphabet = new ArrayList<>();
    static List<Character> identifiersAlphabet = new ArrayList<>();
    static List<Character> literalsAlphabet = new ArrayList<>();
    static List<Character> operators = new ArrayList<>();
    static List<Character> separators = new ArrayList<>();
    static String currentString;
    static Hashtable<String, Integer> symboltable = new Hashtable<>();
    static List<Token> pif = new ArrayList<>();

    private static void classifyToken(String currentString, String currentState, int row) {
        if (currentState.equals("state1")) {
            //check if token is keyword
            boolean check = false;
            for (Tokens token : Tokens.values()) {
                if (token.getType().equals("keyword") && token.getToken().equals(currentString)) {
                    check = true;
                    String code = TokenCodes.Keyword.getCode();
                    int tokenid = token.getTokenId();
                    pif.add(new Token(currentString, row, code, tokenid));
                    break;
                }
            }
            if (!check) {
                if (!symboltable.containsKey(currentString))
                    symboltable.put(currentString, row);
                String code = TokenCodes.Identifier.getCode();
                pif.add(new Token(currentString, row, code, -1));
            }
        }
        if (currentState.equals("state2")) {
            pif.add(new Token(currentString, row, TokenCodes.NumberLiteral.getCode(), -1));
        }
        if (currentState.equals("state4")) {
            pif.add(new Token(currentString, row, TokenCodes.StringLiteral.getCode(), -1));
        }
        if (currentState.equals("state5")) {
            for (Tokens token : Tokens.values())
                if (token.getToken().equals(currentString)) {
                    pif.add(new Token(currentString, row, TokenCodes.Operator.getCode(), token.getTokenId()));
                    break;
                }
        }
        if (currentState.equals("state6")) {
            for (Tokens token : Tokens.values())
                if (token.getToken().equals(currentString)) {
                    pif.add(new Token(currentString, row, TokenCodes.Operator.getCode(), token.getTokenId()));
                    break;
                }
        }
        if (currentState.equals("state7")) {
            for (Tokens token : Tokens.values())
                if (token.getToken().equals(currentString)) {
                    pif.add(new Token(currentString, row, TokenCodes.Separator.getCode(), token.getTokenId()));
                    break;
                }
        }
    }

    public static void writeHashtable(Hashtable<String, Integer> hashtable) throws IOException {
        FileWriter fileWriter = new FileWriter("symboltable.txt");
        List<String> list = Collections.list(hashtable.keys());
        Collections.sort(list);
        for (String identifier : list) {
            fileWriter.write(identifier + '\n');
        }
        fileWriter.close();
    }

    public static void writePif() throws IOException {
        FileWriter fileWriter = new FileWriter("pif.csv");
        for (Token token : pif) {
            String id = String.valueOf(token.getTokenId());
            if (id.equals("-1"))
                id = "#";
            fileWriter.write(token.getTokenCode() + ", " + token.getRow() + ", " + id + ", " + token.getToken() + "\n");
        }
        fileWriter.close();
    }

    private static void generateAlphabet() {
        for (char c = 'a'; c != 'z'; c++)
            identifiersAlphabet.add(c);
        identifiersAlphabet.add('z');
        for (char c = '0'; c != '9'; c++)
            numericAlphabet.add(c);
        numericAlphabet.add('9');
        literalsAlphabet.addAll(numericAlphabet);
        literalsAlphabet.add(' ');
        for (char c = 'A'; c != 'Z'; c++)
            identifiersAlphabet.add(c);
        identifiersAlphabet.add('Z');
        literalsAlphabet.addAll(identifiersAlphabet);
        literalsAlphabet.add(' ');
        literalsAlphabet.add('\"');
        operators.add('=');
        operators.add('!');
        operators.add('&');
        operators.add('<');
        operators.add('+');
        operators.add('-');
        operators.add('*');
        operators.add('/');
        separators.add('(');
        separators.add(')');
        separators.add('{');
        separators.add('}');
        separators.add('[');
        separators.add(']');
        separators.add('.');
        separators.add(',');
        separators.add(';');
        separators.add(':');
        alphabet.addAll(identifiersAlphabet);
        alphabet.addAll(numericAlphabet);
        alphabet.addAll(literalsAlphabet);
        alphabet.addAll(operators);
        alphabet.addAll(separators);
    }

    private static String transition(String currentState, char symbol) {
        if (!alphabet.contains(symbol))
            return "error";
        if (currentState.equals("state0")) {
            if (identifiersAlphabet.contains(symbol) && symbol != '_' && !numericAlphabet.contains(symbol))
                return "state1";
            if (numericAlphabet.contains(symbol))
                return "state2";
            if (symbol == '"')
                return "state3";
            if (operators.contains(symbol))
                return "state5";
            if(symbol == '/')
                return "state8";
            if (separators.contains(symbol))
                return "state7";
            return "error";
        }
        if (currentState.equals("state1")) {
            if (identifiersAlphabet.contains(symbol))
                return "state1";

            return "error";
        }
        if (currentState.equals("state2")) {
            if (numericAlphabet.contains(symbol))
                return "state2";
            return "error";
        }
        if (currentState.equals("state3")) {
            if (symbol == '"')
                return "state4";
            if (literalsAlphabet.contains(symbol))
                return "state3";
            return "error";
        }
        if (currentState.equals("state5")) {
            if (symbol == '&')
                return "state6";
            return "error";
        }
        if (currentState.equals("state6"))
            return "error";
        if (currentState.equals("state7"))
            return "error";
        if(currentState.equals("state8")){
            if(symbol == '/')
                return "state9";
            return "error";
        }
        if(currentState.equals("state9"))
            return "state9";
        if (symbol == ' ')
            return "error";
        return "error";

    }

    public static void main(String[] args) {

        //RegEx identifier [a-zA-Z][a-zA-Z0-9\\_]*
        //RegEx integerLiteral [0-9]+
        //RegEx stringLiteral \"[a-zA-Z0-9 ]*\"
        //RegEx operator !|&&|<|\\+|\\-|\\*|\\/
        //RegEx separator \{|\}|\[|\]|\(|\)|\.|\,|\:|\;
        try {
            File f = new File("Product.java");
            //File f = new File(args[0]);
            Scanner scanner = new Scanner(f);
            List<String> states = Arrays.asList("state0", "state1", "state2", "state3", "state4", "state5", "state6", "state7");
            List<String> finalStates = Arrays.asList("state1", "state2", "state4", "state5", "state6", "state7");
            generateAlphabet();
            int row = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String currentState = states.get(0);
                currentString = "";
                char[] input = line.toCharArray();
                for (int i = 0; i < line.length(); i++) {
                    char symbol = input[i];

                    if (transition(currentState, symbol).equals("error")) {
                        if (finalStates.contains(currentState))
                            classifyToken(currentString, currentState, row);
                        currentState = states.get(0);
                        currentString = "";
                    }

                    currentState = transition(currentState, symbol);
                    currentString += String.valueOf(symbol);

                }
                if (finalStates.contains(currentState))
                    classifyToken(currentString, currentState, row);
                row++;
            }
            scanner.close();
            writeHashtable(symboltable);
            writePif();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
