package cn.edu.njnu.Reporsitory;

import cn.edu.njnu.pojo.EntityNode;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RelationRepository {
    @Query("MATCH (a:concept) -[]-> (m:concept) where a.name = { name } RETURN m.name AS name limit 5")
    List<EntityNode> findUserRelationByEachId(@Param("name") String name);
}
