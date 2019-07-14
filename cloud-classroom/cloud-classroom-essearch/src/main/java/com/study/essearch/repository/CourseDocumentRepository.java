package com.study.essearch.repository;

import com.study.essearch.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author andy
 * @version 0.1
 * @date 2018/12/13 17:35
 */
@Repository
public interface CourseDocumentRepository extends ElasticsearchRepository<CourseDocument,String> {
}
