package com.blockchain.base;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;

public class RelationUtil {
  private static Map<String, Integer> entityTypeMap = new HashMap<>();
  private static Map<String, Integer> relationTypeMap = new HashMap<>();
  private static Map<Integer, String> entityTypeStrMap = new HashMap<>();
  private static Map<Integer, String> relationTypeStrMap = new HashMap<>();

  static {
    try {
      File file = new ClassPathResource("/entity_type.txt").getFile();
      List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
      for (int i = 0; i < lines.size(); i++) {
        entityTypeMap.put(lines.get(i).trim(), i);
      }
      for (int i = 0; i < lines.size(); i++) {
        entityTypeStrMap.put(i, lines.get(i).trim());
      }

      file = new ClassPathResource("/relation_type.txt").getFile();
      lines = Files.readAllLines(Paths.get(file.toURI()));
      for (int i = 0; i < lines.size(); i++) {
        relationTypeMap.put(lines.get(i).trim(), i);
      }
      for (int i = 0; i < lines.size(); i++) {
        relationTypeStrMap.put(i, lines.get(i).trim());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Set<String> getEntityKeys() {
    return entityTypeMap.keySet();
  }

  public static Set<String> getRelationKeys() {
    return relationTypeMap.keySet();
  }

  public static Map<String, Integer> getRelationTypes() {
    return relationTypeMap;
  }

  public static Integer getEntityType(String key) {
    return entityTypeMap.get(key);
  }

  public static Integer getRelationType(String key) {
    return relationTypeMap.get(key);
  }

  public static String getEntityTypeStr(Integer key) {
    return entityTypeStrMap.get(key);
  }

  public static String getRelationTypeStr(Integer key) {
    return relationTypeStrMap.get(key);
  }

  private static void genNodeSets() {
    String node = "                \"entityType\": tfgnn.NodeSetSpec(";
    String content = """
                    {
                        "features": {
                            "hidden_state": tf.TensorSpec(
                                shape=(None, 10), dtype=tf.float64, name=None
                            ),
                            "label": tf.TensorSpec(
                                shape=(None, 1), dtype=tf.int64, name=None
                            ),
                        },
                        "sizes": tf.TensorSpec(
                            shape=(1,), dtype=tf.int32, name=None
                        ),
                    },
                    tf.TensorShape([]),
                    tf.int32,
                    None,
                    ),
        """;
    Iterator<String> it = entityTypeMap.keySet().iterator();
    String entityType = null;
    while (it.hasNext()) {
      entityType = it.next();
      System.out.println(node.replace("entityType", entityType));
      System.out.print(content);
    }
  }

  private static void genEdgeSets() {
    String edge1 = "                \"srcNodeType-trgNodeType-edgeType\": tfgnn.EdgeSetSpec(";
    String edge2 = "                            {\"#index.0\": \"srcNodeType\", \"#index.1\": \"trgNodeType\"},";
    String content1 = """
                                       {
                        "features": {
                            "hidden_state": tf.TensorSpec(
                                shape=(None, 1), dtype=tf.float64, name=None
                            )
                        },
                        "sizes": tf.TensorSpec(
                            shape=(1,), dtype=tf.int32, name=None
                        ),
                        "adjacency": tfgnn.AdjacencySpec(
                            {
                                "#index.0": tf.TensorSpec(
                                    shape=(None,), dtype=tf.int64, name=None
                                ),
                                "#index.1": tf.TensorSpec(
                                    shape=(None,), dtype=tf.int64, name=None
                                ),
                            },
                            tf.TensorShape([]),
                            tf.int32,
        """;
    String content2 = """
                                ),
                    },
                    tf.TensorShape([]),
                    tf.int32,
                    None,
                ),
        """;
    Iterator<String> it = entityTypeMap.keySet().iterator();
    String entityType = null;
    Iterator<String> it2 = entityTypeMap.keySet().iterator();
    String entityType2 = null;
    Iterator<Integer> it3 = relationTypeStrMap.keySet().iterator();
    Integer edgeType = null;

    while (it.hasNext()) {
      entityType = it.next();
      while (it2.hasNext()) {
        entityType2 = it2.next();
        while (it3.hasNext()) {
          edgeType = it3.next();
          System.out.println(edge1.replace("srcNodeType", entityType).replace("trgNodeType", entityType2).replace("edgeType", String.valueOf(edgeType)));
          System.out.println(content1);
          System.out.println(edge2.replace("srcNodeType", entityType).replace("trgNodeType", entityType2));
          System.out.print(content2);
        }
       it3 = relationTypeStrMap.keySet().iterator();
      }
      it2 = entityTypeMap.keySet().iterator();
    }
  }

  public static void main(String[] args) {
    genNodeSets();
    //genEdgeSets();
  }
}
