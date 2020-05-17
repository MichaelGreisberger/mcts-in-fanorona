package Fanorona.Board;

public class BoardStateExamples {


    /*
    010000
    000000000000000000
    000010100001100110
    000000000000000000
    000100101000100000
    000000000000000000
     */
    public static final String PAPER_EXAMPLE_STATE = "@\u0000\u0000\n\u0019\u0080\u0000\u0001(\u0080\u0000\u0000";

    /*
    Black's turn
  a   b   c   d   e   f   g   h   i
0   - B -   - B -   - B -   - B -
  | \ | / | \ | / | \ | / | \ | / |
1 B -   -   -   -   -   -   -   - B
  | / | \ | / | \ | / | \ | / | \ |
2 B -   - W -   -   -   - W -   - W
  | \ | / | \ | / | \ | / | \ | / |
3 W - W -   -   -   -   -   -   - W
  | / | \ | / | \ | / | \ | / | \ |
4 W - W -   -   -   -   -   - W -
     */
    public static final String EXAMPLE_STATE_1 = "\u0080\u0088\u0088\u0080 ¡\u0001\u0015 \u0005@\u0004";

    /*
Black's turn
  a   b   c   d   e   f   g   h   i
0 B - B - B - B - B - B -   - B - B
  | \ | / | \ | / | \ | / | \ | / |
1 B - B - B - B - B -   - B - B - B
  | / | \ | / | \ | / | \ | / | \ |
2 B - W - B - W - W - B - W - B - W
  | \ | / | \ | / | \ | / | \ | / |
3 W - W - W -   - W - W - W - W - W
  | / | \ | / | \ | / | \ | / | \ |
4 W - W - W - W - W - W - W - W - W
s
     */
    public static final String EXAMPLE_STATE_2 = "\u0082ª\u008Aª\u008A¦Y\u0095EUUU";

    public static final String EXMPLE_STATE_S64_1 = "QsKIwoooAcKgAABQBAQA";

    public static final String APPROACH_ACTION = "c2d3A";
    /*
White's turn
  a   b   c   d   e   f   g   h   i
0 B - W - B - B - B - W -   - B -
  | \ | / | \ | / | \ | / | \ | / |
1 W -   -   - B - W - B - B - B - W
  | / | \ | / | \ | / | \ | / | \ |
2   - B - W - W -   - B - B - W - B
  | \ | / | \ | / | \ | / | \ | / |
3   - W - W -   -   - W - W -   - W
  | / | \ | / | \ | / | \ | / | \ |
4 B - W - B -   - B - W -   -   - B

     */
    public static final String APPROACH_BEFORE_B64 = "woLCoMKqAGrCpgHClUFVUVU=";
    /*
Black's turn
  a   b   c   d   e   f   g   h   i
0 B - W - B - B - B - W -   - B -
  | \ | / | \ | / | \ | / | \ | / |
1 W -   -   - B - W - B - B - B - W
  | / | \ | / | \ | / | \ | / | \ |
2   - B -   - W -   - B - B - W - B
  | \ | / | \ | / | \ | / | \ | / |
3   - W - W - W -   - W - W -   - W
  | / | \ | / | \ | / | \ | / | \ |
4 B - W - B -   -   - W -   -   - B

     */
    public static final String APPROACH_AFTER_B64 = "QsKgwqoAasKkAcKVYVVQVQ==";
    public static final String WITHDRAW_ACTION = "a3a4W";
    /*
White's turn
  a   b   c   d   e   f   g   h   i
0 B - B -   - B -   - B - B - B - B
  | \ | / | \ | / | \ | / | \ | / |
1 B - B - B -   - B -   - B - B - B
  | / | \ | / | \ | / | \ | / | \ |
2 B -   -   -   - B -   -   - B - W
  | \ | / | \ | / | \ | / | \ | / |
3 W -   - W -   - W -   - W -   - W
  | / | \ | / | \ | / | \ | / | \ |
4   - W -   -   -   - W - W - W -

     */
    public static final String WITHDRAW_BEFORE_B64  = "QsKIwqrCqMKKwqAgwpREREBU";

    /*
    Black's turn
  a   b   c   d   e   f   g   h   i
0   - B -   - B -   - B - B - B - B
  | \ | / | \ | / | \ | / | \ | / |
1   - B - B -   - B -   - B - B - B
  | / | \ | / | \ | / | \ | / | \ |
2   -   -   -   - B -   -   - B - W
  | \ | / | \ | / | \ | / | \ | / |
3   -   - W -   - W -   - W -   - W
  | / | \ | / | \ | / | \ | / | \ |
4 W - W -   -   -   - W - W - W -

     */
    public static final String WITHDRAW_AFTER_B64  = "woDCiMKqKMKKwoAgwpBERUBU";


    public static final String PAIKA_ACTION = "b2b3P";
    /*
    Black's turn
  a   b   c   d   e   f   g   h   i
0   - B -   - B -   -   -   -   -
  | \ | / | \ | / | \ | / | \ | / |
1   - B -   -   -   -   - W -   -
  | / | \ | / | \ | / | \ | / | \ |
2   - B -   -   -   -   -   -   - W
  | \ | / | \ | / | \ | / | \ | / |
3   -   -   -   -   -   -   -   - W
  | / | \ | / | \ | / | \ | / | \ |
4   -   -   -   -   -   -   - W -

     */
    public static final String PAIKA_BEFORE_B64  = "woDCiAAgBAgAEAAEAAQ=";
    /*
    White's turn
  a   b   c   d   e   f   g   h   i
0   - B -   - B -   -   -   -   -
  | \ | / | \ | / | \ | / | \ | / |
1   - B -   -   -   -   - W -   -
  | / | \ | / | \ | / | \ | / | \ |
2   -   -   -   -   -   -   -   - W
  | \ | / | \ | / | \ | / | \ | / |
3   - B -   -   -   -   -   -   - W
  | / | \ | / | \ | / | \ | / | \ |
4   -   -   -   -   -   -   - W -

     */
    public static final String PAIKA_AFTER_B64  = "QMKIACAEAAASAAQABA==";

    public static final String EXTENDED_ACTION = "c1c2Ab3Ab2W";
    /*
    Black's turn3
      a   b   c   d   e   f   g   h   i
    0   - B -   - B -   - B - B - B - B
      | \ | / | \ | / | \ | / | \ | / |
    1   - B - B -   - B -   - B - B - B
      | / | \ | / | \ | / | \ | / | \ |
    2   -   -   -   - B -   -   - B - W
      | \ | / | \ | / | \ | / | \ | / |
    3   -   - W -   - W -   - W -   - W
      | / | \ | / | \ | / | \ | / | \ |
    4 W - W -   -   -   - W - W - W -

     */
    public static final String EXTENDED_BEFORE_B64  = "woDCiMKqKMKKwoAgwpBERUBU";
    /*
    White's turn
  a   b   c   d   e   f   g   h   i
0   - B -   - B -   - B - B - B - B
  | \ | / | \ | / | \ | / | \ | / |
1   - B -   -   - B -   - B - B - B
  | / | \ | / | \ | / | \ | / | \ |
2   - B -   -   - B -   -   - B - W
  | \ | / | \ | / | \ | / | \ | / |
3   -   -   -   - W -   - W -   - W
  | / | \ | / | \ | / | \ | / | \ |
4   -   -   -   -   - W - W - W -

     */
    public static final String EXTENDED_AFTER_B64  = "QMKIwqogworCiCDCkAREAFQ=";


}
