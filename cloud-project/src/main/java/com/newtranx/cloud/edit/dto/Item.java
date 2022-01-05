package com.newtranx.cloud.edit.dto;

import lombok.Data;

@Data
public class Item {
    //    {"text":
//        "范志民男，汉族，1921年2月出生，五莲县院西乡范家车村人。系中国美术协会会员、获首届邹韬奋出版奖的全国十大美术编辑之一。曾编辑出版《中国画家丛书》20余种，中国美术史论10余种，并任《中国美术全集》中《古代版画》的副主编，编纂大型专业工具书《中国美术家名人辞典》、《古代画汇览》，其版画、国画、水粉画作品多次选入各级展览。",
//                "items": [{"item": "范志民", "wordtag_label": "人物类_实体", "offset": 0, "length": 3}, {"item": "五莲县", "wordtag_label":
//        "世界地区类", "offset": 18, "length": 3}, {"item": "院西乡", "wordtag_label": "世界地区类", "offset": 21, "length": 3}, {"item":
//        "范家车村", "wordtag_label": "世界地区类", "offset": 24, "length": 4}, {"item": "中国美术协会", "wordtag_label": "组织机构类", "offset": 31,
//            "length": 6}, {"item": "邹韬奋", "wordtag_label": "人物类_实体", "offset": 43, "length": 3}], "person_triples": [{"name": "范志民",
//            "ethnic": "汉族", "gender": "男", "origin": "五莲县院西乡范家车村"}]}

    private String item;
    private String wordtag_label;
    private Integer offset;
    private Integer length;
}
