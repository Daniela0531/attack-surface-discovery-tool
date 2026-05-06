package org.example.result_structure;

public class ResultEdge {
    ResultNode from;
    ResultNode to;
//    Condition condition;

    public ResultEdge(ResultNode from, ResultNode to) {
        this.from = from;
        this.to = to;
    }

    public ResultNode getFrom() {
        return from;
    }
    public ResultNode getTo() {
        return to;
    }
}
