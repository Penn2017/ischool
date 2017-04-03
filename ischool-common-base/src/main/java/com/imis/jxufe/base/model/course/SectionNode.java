package com.imis.jxufe.base.model.course;

import java.util.List;

/**
 * 章节树
 *
 * @author zhongping
 * @date 2017/4/3
 */
public class SectionNode {

    private Integer id;

    private String name;

    private List<SectionNode> rows;


    public SectionNode() {
        /**
         * empty
         */
    }

    public SectionNode(Integer id, String name, List<SectionNode> rows) {
        this.id = id;
        this.name = name;
        this.rows = rows;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SectionNode> getRows() {
        return rows;
    }

    public void setRows(List<SectionNode> rows) {
        this.rows = rows;
    }
}
