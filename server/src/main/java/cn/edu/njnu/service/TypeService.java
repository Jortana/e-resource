package cn.edu.njnu.service;

import cn.edu.njnu.mapper.TypeMapper;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TypeService {
    private final TypeMapper typeMapper;

    public TypeService(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public Result classification(){
        JSONArray resArray = new JSONArray();
        JSONObject classOb = new JSONObject();
        classOb.put("condition", "科目");
        classOb.put("classification", kemu());
        resArray.add(classOb);
        JSONObject classOb1 = new JSONObject();
        classOb1.put("condition", "分类条件");
        classOb1.put("classification", null);
        resArray.add(classOb1);
        return ResultFactory.buildSuccessResult("Success", resArray);
    }

    public JSONArray kemu(){
        List<Map> periodList = typeMapper.period();
        JSONArray resArray = new JSONArray();
        for (Map period:periodList){
            JSONObject periodOb = new JSONObject();
            String periodID = Integer.toString((Integer) period.get("period_id"));
            String periodName = (String) period.get("period_name");
            periodOb.put("periodID", Integer.parseInt(periodID));
            periodOb.put("periodName", periodName);
            JSONArray subjectArray = new JSONArray();
            List<Map> subjectList = typeMapper.subject(periodID);
            for (Map subject:subjectList){
                JSONObject subjectOb = new JSONObject();
                int subjectID = (int) subject.get("subject_id");
                String subjectName = (String) subject.get("subject_name");
                subjectOb.put("subjectID", subjectID);
                subjectOb.put("subjectName", subjectName);
                subjectArray.add(subjectOb);
            }
            periodOb.put("subject", subjectArray);
            resArray.add(periodOb);
        }
        return resArray;
    }
}
