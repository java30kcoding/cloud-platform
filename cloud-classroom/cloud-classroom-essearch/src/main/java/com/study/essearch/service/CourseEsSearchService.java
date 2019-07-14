package com.study.essearch.service;

import com.study.essearch.document.CourseDocument;

import java.util.List;

/**
 * @author andy
 * @version 0.1
 * @date 2018/12/13 15:32
 */
public interface CourseEsSearchService extends BaseSearchService<CourseDocument> {
    /**
     * 保存
     * @auther: andy
     * @date: 2018/12/13 16:02
     */
    void save(CourseDocument... courseDocuments);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 清空索引
     */
    void deleteAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    CourseDocument getById(String id);

    /**
     * 查询全部
     * @return
     */
    List<CourseDocument> getAll();
}
