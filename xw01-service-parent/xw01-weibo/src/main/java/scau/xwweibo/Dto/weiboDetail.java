package scau.xwweibo.Dto;

import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Users;
import scau.xwcommon.entity.Weibos;

import java.util.List;

public class weiboDetail {
    private int wbId;
    private String wbUserLoginName;
    private String wbTitle;
    private String wbContent;
    private int wbReadCount;
    private String wbImg;
    private Users author;
    private List<Weibos> weibosList;
    private List<Comments> commentsList;


}
