package com.example.result_structure;

import com.example.analizer.followed_data.FollowedDatum;

public class ResultEdge {
    FollowedDatum from;
    FollowedDatum to;
//    Condition condition;

    public ResultEdge(FollowedDatum from, FollowedDatum to) {
        this.from = from;
        this.to = to;
    }

    public FollowedDatum getFrom() {
        return from;
    }
    public FollowedDatum getTo() {
        return to;
    }
}

//public class ResultEdge {
//    ResultNode from;
//    ResultNode to;
////    Condition condition;
//
//    public ResultEdge(ResultNode from, ResultNode to) {
//        this.from = from;
//        this.to = to;
//    }
//
//    public ResultNode getFrom() {
//        return from;
//    }
//    public ResultNode getTo() {
//        return to;
//    }
//}
